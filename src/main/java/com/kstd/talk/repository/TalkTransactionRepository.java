package com.kstd.talk.repository;

import com.kstd.talk.api.error.ErrorCode;
import com.kstd.talk.api.error.ResponseException;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.entity.QTalkList;
import com.kstd.talk.entity.QTalkMem;
import com.kstd.talk.util.DateUtil;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class TalkTransactionRepository {

    private final JPAQueryFactory factory;
    private final TalkIdSeqRepository talkIdSeqRepository;

    /**
     * 데이터 생성
     *
     * @param talkBaseDto 강연정보
     * @return talkList
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public TalkBaseDto insert(TalkBaseDto talkBaseDto) {
        log.debug("talkBaseDto: {}", talkBaseDto);
        QTalkList qTalkList = QTalkList.talkList;
        String talkId = createTalkId();
        factory.insert(qTalkList)
                .columns(
                        qTalkList.talkId,
                        qTalkList.speaker,
                        qTalkList.startDtm,
                        qTalkList.place,
                        qTalkList.seat,
                        qTalkList.talkDesc
                )
                .values(
                        talkId,
                        talkBaseDto.getSpeaker(),
                        DateUtil.strToDt(talkBaseDto.getStartDtm()),
                        talkBaseDto.getPlace(),
                        talkBaseDto.getSeat(),
                        talkBaseDto.getDesc()
                )
                .execute();
        talkBaseDto.setTalkId(talkId);
        return talkBaseDto;
    }

    // 강연 채번
    private String createTalkId() {
        Integer id = talkIdSeqRepository.createTalkId();
        return String.format("T%05d", id);
    }

    /**
     * 수강 등록
     *
     * @param talkId 강연ID
     * @param memId  사번
     * @return 등록정보
     */
    @Transactional
    public TalkMemberDto register(String talkId, String memId) {
        if (!hasSeat(talkId)) {
            log.error("talkId( {} ) has no seat", talkId);
            throw new ResponseException(ErrorCode.NO_SEAT);
        }
        TalkMemberDto talkMemberDto = new TalkMemberDto(talkId, memId, TalkState.REGISTERATION);
        QTalkMem qTalkMem = QTalkMem.talkMem;
        long execute = factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId,
                        qTalkMem.memId,
                        qTalkMem.state
                )
                .values(
                        talkId,
                        memId,
                        TalkState.REGISTERATION
                )
                .execute();
        if (execute < 1) {
            log.error("failed to register(talkId: {}, memId: {})", talkId, memId);
            throw new ResponseException(ErrorCode.NO_SEAT);
        }
        return talkMemberDto;
    }

    /**
     * 수강 취소
     *
     * @param talkId 강연ID
     * @param memId  사번
     * @return 등록정보
     */
    @Transactional
    public TalkMemberDto cancel(String talkId, String memId) {
        QTalkMem qTalkMem = QTalkMem.talkMem;
        long execute = factory.update(qTalkMem)
                .set(qTalkMem.state, TalkState.CANCEL)
                .where(qTalkMem.talkId.eq(talkId))
                .where(qTalkMem.memId.eq(memId))
                .where(qTalkMem.state.eq(TalkState.REGISTERATION))
                .execute();
        if (execute == 0) {
            log.error("failed to cancel memId: {}, talkId: {}", memId, talkId);
            throw new ResponseException(ErrorCode.NO_DATA);
        }
        return new TalkMemberDto(talkId, memId, TalkState.CANCEL);
    }

    private boolean hasSeat(String talkId) {
        QTalkList qTalkList = QTalkList.talkList;
        QTalkMem qTalkMem = QTalkMem.talkMem;
        Tuple tuple = factory
                .select(
                        qTalkList.seat.coalesce(0).as("seat"),
                        qTalkMem.memId.count().as("memCnt")
                )
                .from(qTalkList)
                .leftJoin(qTalkMem)
                .on(qTalkList.talkId.eq(qTalkMem.talkId))
                .where(qTalkList.talkId.eq(talkId))
                .setLockMode(LockModeType.WRITE) // READ/WRITE 모두 lock
                .fetchOne();
        Integer seat = tuple.get(0, Integer.class);
        Long memCnt = tuple.get(1, Long.class);
        if (seat == null || memCnt == null) {
            log.error("data is null(tuple(seat, memCnt): {})", tuple);
            throw new ResponseException(ErrorCode.NO_DATA);
        }
        log.info("seat: {}, memCnt: {}", seat, memCnt);
        return seat.intValue() > memCnt.intValue();
    }
}
