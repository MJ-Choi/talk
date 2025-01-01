package com.kstd.talk.repository;

import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.entity.QTalkMem;
import com.kstd.talk.entity.TalkMem;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class TalkMemFinder {

    private final JPAQueryFactory factory;

    /**
     * 강연 신청자 목록 조회
     *
     * @param offset   n번째 페이지
     * @param pageSize 페이지 사이즈
     * @return 강연 별 수강자 목록
     */
    public List<Tuple> getMembers(long offset, int pageSize) {
        QTalkMem qTalkMem = QTalkMem.talkMem;
        return factory
                .select(
                        qTalkMem.talkId,
                        qTalkMem.memId
                )
                .from(qTalkMem)
                .where(qTalkMem.state.eq(TalkState.REGISTERATION))
                .offset(offset)
                .limit(pageSize)
                .fetch();
    }

    /**
     * 강연 신청자 목록 조회
     *
     * @param talkId   강연ID
     * @return 수강자 목록
     */
    public List<String> getMembers(String talkId) {
        QTalkMem qTalkMem = QTalkMem.talkMem;
        return factory
                .select(
                        qTalkMem.memId
                )
                .from(qTalkMem)
                .where(qTalkMem.talkId.eq(talkId))
                .where(qTalkMem.state.eq(TalkState.REGISTERATION))
                .fetch();
    }

    /**
     * 회원 조회
     * @param talkId 강연ID
     * @param memId 회원ID
     * @return
     */
    public TalkMem getMember(String talkId, String memId) {
        QTalkMem qTalkMem = QTalkMem.talkMem;
        return factory
                .select(
                        qTalkMem
                )
                .from(qTalkMem)
                .where(qTalkMem.talkId.eq(talkId))
                .where(qTalkMem.memId.eq(memId))
                .fetchOne();
    }

    /**
     * 신청 내역 조회
     * @param memId 사번
     * @return 신청 내역
     */
    public List<TalkMemberDto> getTalkLogs(String memId) {
        QTalkMem qTalkMem = QTalkMem.talkMem;
        return factory.select(Projections.constructor(TalkMemberDto.class,
                        qTalkMem.talkId,
                        qTalkMem.memId,
                        qTalkMem.state))
                .from(qTalkMem)
                .where(qTalkMem.memId.eq(memId))
                .fetch();
    }
}
