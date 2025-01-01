package com.kstd.talk.repository;

import com.kstd.talk.config.TestConfig;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.entity.QTalkMem;
import com.kstd.talk.util.DateUtil;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 이 테스트는 init.sql 로 초기화 된 상태에서만 정상동작
 */
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TalkFinder.class, TalkMemFinder.class, TestConfig.class})
class TalkFinderTest {

    @Autowired
    private TalkFinder talkFinder;
    @Autowired
    private TalkMemFinder talkMemFinder;
    @Autowired
    private JPAQueryFactory factory;

    @BeforeEach
    void init() {
        // 강연 확인
        Assertions.assertNotNull(talkFinder.getTalkInfo("T00001"));
        Assertions.assertNotNull(talkFinder.getTalkInfo("T00002"));
        Assertions.assertNotNull(talkFinder.getTalkInfo("T00003"));
    }

    @Test
    @DisplayName("강연 전체 조회")
    void getList() {
        Pageable pageable = PageRequest.of(0, 1);
        List<TalkBaseDto> list = talkFinder.getList(pageable.getOffset(), pageable.getPageSize());
        log.info("list({}): {}", list.size(), list);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(1, list.size());

        Pageable pageable2 = PageRequest.of(0, 3);
        List<TalkBaseDto> list2 = talkFinder.getList(pageable2.getOffset(), pageable2.getPageSize());
        log.info("list2({}): {}", list2.size(), list2);
        Assertions.assertNotNull(list2);
        Assertions.assertEquals(3, list2.size());
    }

    @Test
    @DisplayName("날짜기준으로 수강 신청 가능한 강연 목록 조회")
    void getPossibleLists() {
        String baseDtm = "2025-01-01 11:00";
        LocalDateTime baseDateTime = DateUtil.strToDt(baseDtm);
        LocalDateTime afterDay = baseDateTime.minusDays(1);
        LocalDateTime maxDay = baseDateTime.plusDays(7);
        Pageable pageable = PageRequest.of(0, 3);
        // T00001 2025-01-05 10:00
        // T00002 2025-01-03 10:00
        // T00003 2024-12-31 10:00
        List<TalkBaseDto> possibleLists = talkFinder.getPossibleLists(afterDay, maxDay, pageable.getOffset(), pageable.getPageSize());
        log.info("list({}): {}", possibleLists.size(), possibleLists);
        Assertions.assertNotNull(possibleLists);
        Assertions.assertEquals(2, possibleLists.size());
    }

    @Test
    @DisplayName("인기 강연 조회")
    void getPopularLists() {
        //강연 수강등록
        registerMember();
        List<Tuple> members = talkMemFinder.getMembers(0, 10);
        log.info("members:{}:{}", members.size(), members);
        Assertions.assertEquals(4, members.size());

        String baseDtm = "2025-01-01 11:00";
        LocalDateTime baseDateTime = DateUtil.strToDt(baseDtm);
        LocalDateTime minDay = baseDateTime.minusDays(3); // 2024-12-29 11:00 ~ 2025-01-01 11:00
        List<TalkBaseDto> lists = talkFinder.getPopularLists(minDay, baseDateTime, 0, 3);
        log.info("lists:({}): {}", lists.size(), lists);
        Assertions.assertNotNull(lists);
        Assertions.assertEquals(2, lists.size());
        Assertions.assertEquals(2, lists.get(0).getParticipants());
        Assertions.assertEquals(1, lists.get(1).getParticipants());

    }

    @Test
    @DisplayName("강연 정보 조회(테스트 코드에서만 사용)")
    void getTalkInfo() {
        String talkId1 = "T00001";
        String talkId2 = "T00002";
        String talkId3 = "T00003";
        TalkBaseDto talkInfo1 = talkFinder.getTalkInfo(talkId1);
        Assertions.assertEquals(talkId1, talkInfo1.getTalkId());
        TalkBaseDto talkInfo2 = talkFinder.getTalkInfo(talkId2);
        Assertions.assertEquals(talkId2, talkInfo2.getTalkId());
        TalkBaseDto talkInfo3 = talkFinder.getTalkInfo(talkId3);
        Assertions.assertEquals(talkId3, talkInfo3.getTalkId());
    }

    private void registerMember() {
        QTalkMem qTalkMem = QTalkMem.talkMem;

        // T00003 = 2
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm
                )
                .values(
                        "T00003", "test1", TalkState.REGISTERATION, DateUtil.strToDt("2024-12-30 11:00")
                )
                .execute();
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm
                )
                .values(
                        "T00003", "test2", TalkState.CANCEL, DateUtil.strToDt("2024-12-30 11:00")
                )
                .execute();

        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm
                )
                .values(
                        "T00003", "test3", TalkState.REGISTERATION, DateUtil.strToDt("2024-12-31 11:00")
                )
                .execute();

        // T00002 - 0 (기간만료)
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm, qTalkMem.updateDtm
                )
                .values(
                        "T00002", "test2", TalkState.REGISTERATION, DateUtil.strToDt("2024-12-27 11:00"), DateUtil.strToDt("2024-12-28 11:00")
                )
                .execute();

        // T00001 - 0
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm, qTalkMem.updateDtm
                )
                .values(
                        "T00001", "test1", TalkState.REGISTERATION, DateUtil.strToDt("2024-12-29 12:00"), DateUtil.strToDt("2024-12-29 12:10")
                )
                .execute();
    }
}