package com.kstd.talk.repository;

import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.entity.QTalkList;
import com.kstd.talk.entity.QTalkMem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class TalkFinder {

    private final JPAQueryFactory factory;

    /**
     * 강연 조회
     *
     * @param offset 시작 지점(0 index)
     * @param limit  최대 조회 건수
     * @return 강연 목록
     */
    public List<TalkBaseDto> getList(long offset, int limit) {
        log.debug("offset: {}, limit: {}", offset, limit);
        QTalkList qTalkList = QTalkList.talkList;
        QTalkMem qTalkMem = QTalkMem.talkMem;
        return factory
                .select(Projections.constructor(TalkBaseDto.class,
                                qTalkList.talkId,
                                qTalkList.speaker,
                                qTalkList.place,
                                qTalkList.seat,
                                qTalkMem.memId.count().as("participants"),
                                qTalkList.startDtm,
                                qTalkList.talkDesc
                        )
                )
                .from(qTalkList)
                .leftJoin(qTalkMem).on(qTalkList.talkId.eq(qTalkMem.talkId))
                .groupBy(qTalkList.talkId)
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    /**
     * 수강 신청 가능한 강연 조회 (잔여석은 무시)
     *
     * @param afterDay 강연 시작일을 기준으로 노출 불가일 (ex. 조회일을 기준으로 강연 시작일이 1일이 지나면 노출 되지 않는다)
     * @param maxDay   강연 시작일을 기준으로 최대 노출 가능일 (ex. 조회일을 기준으로 강연 시작일이 7일 전이면 노출한다.)
     * @param offset   offset
     * @param limit    페이지 사이즈
     * @return
     */
    public List<TalkBaseDto> getPossibleLists(LocalDateTime afterDay, LocalDateTime maxDay, long offset, int limit) {
        QTalkList qTalkList = QTalkList.talkList;
        QTalkMem qTalkMem = QTalkMem.talkMem;
        return factory
                .select(Projections.constructor(TalkBaseDto.class,
                                qTalkList.talkId,
                                qTalkList.speaker,
                                qTalkList.place,
                                qTalkList.seat,
                                qTalkMem.memId.count().as("participants"),
                                qTalkList.startDtm,
                                qTalkList.talkDesc
                        )
                )
                .from(qTalkList)
                .leftJoin(qTalkMem).on(qTalkList.talkId.eq(qTalkMem.talkId))
                .groupBy(qTalkList.talkId)
                .where(qTalkList.startDtm.between(afterDay, maxDay))
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    /**
     * 인기 강연 조회
     *
     * @param minDay 최소일
     * @param maxDay 최대일
     * @param offset offset
     * @param limit  limit
     * @return
     */
    public List<TalkBaseDto> getPopularLists(LocalDateTime minDay, LocalDateTime maxDay, long offset, int limit) {
        QTalkList qTalkList = QTalkList.talkList;
        QTalkMem qTalkMem = QTalkMem.talkMem;
        return factory
                .select(Projections.constructor(TalkBaseDto.class,
                                qTalkList.talkId,
                                qTalkList.speaker,
                                qTalkList.place,
                                qTalkList.seat,
                                qTalkMem.memId.count().as("participants"),
                                qTalkList.startDtm,
                                qTalkList.talkDesc
                        )
                )
                .from(qTalkList)
                .leftJoin(qTalkMem).on(qTalkList.talkId.eq(qTalkMem.talkId))
                .where(qTalkMem.updateDtm.between(minDay, maxDay))
                .where(qTalkMem.state.eq(TalkState.REGISTERATION))
                .groupBy(qTalkList.talkId)
                .orderBy(qTalkMem.memId.count().desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    /**
     * 테스트 코드에서 강연 정보를 확인하기 위한 용도
     *
     * @param talkId 강연ID
     * @return 강연 정보
     */
    public TalkBaseDto getTalkInfo(String talkId) {
        QTalkList qTalkList = QTalkList.talkList;
        return factory
                .select(Projections.constructor(TalkBaseDto.class,
                                qTalkList.talkId,
                                qTalkList.speaker,
                                qTalkList.place,
                                qTalkList.seat,
                                qTalkList.startDtm,
                                qTalkList.talkDesc
                        )
                )
                .from(qTalkList)
                .where(qTalkList.talkId.eq(talkId))
                .fetchOne();
    }

}
