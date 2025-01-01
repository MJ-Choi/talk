package com.kstd.talk.repository;

import com.kstd.talk.config.TestConfig;
import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.entity.QTalkMem;
import com.kstd.talk.entity.TalkMem;
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

import java.util.List;

/**
 * 이 테스트는 init.sql 로 초기화 된 상태에서만 정상동작
 */
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TalkFinder.class, TalkMemFinder.class, TestConfig.class})
class TalkMemFinderTest {

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
        // 강연 회원 추가
        QTalkMem qTalkMem = QTalkMem.talkMem;
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state
                )
                .values(
                        "T00001", "test1", TalkState.REGISTERATION
                )
                .execute();
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state
                )
                .values(
                        "T00001", "test2", TalkState.CANCEL
                )
                .execute();

        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state
                )
                .values(
                        "T00001", "test3", TalkState.REGISTERATION
                )
                .execute();
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state
                )
                .values(
                        "T00002", "test1", TalkState.REGISTERATION
                )
                .execute();
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state
                )
                .values(
                        "T00002", "test2", TalkState.REGISTERATION
                )
                .execute();
    }

    @Test
    @DisplayName("전체 강연 신청자 목록 조회")
    void getMembers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Tuple> members = talkMemFinder.getMembers(pageable.getOffset(), pageable.getPageSize());
        log.info("members({}): {}", members.size(), members);
        Assertions.assertNotNull(members);
        Assertions.assertEquals(4, members.size());
    }

    @Test
    @DisplayName("강연ID로 강연 신청자 목록 조회")
    void getMembersByTalkId() {
        String talkId1 = "T00001";
        List<String> membesr1 = talkMemFinder.getMembers(talkId1);
        log.info("members1({}): {}", membesr1.size(), membesr1);
        Assertions.assertNotNull(membesr1);
        Assertions.assertEquals(2, membesr1.size());

        String talkId2 = "T00002";
        List<String> members2 = talkMemFinder.getMembers(talkId2);
        log.info("members2({}): {}", members2.size(), members2);
        Assertions.assertNotNull(members2);
        Assertions.assertEquals(2, members2.size());

        String talkId3 = "T00003";
        List<String> members3 = talkMemFinder.getMembers(talkId3);
        log.info("members3({}): {}", members3.size(), members3);
        Assertions.assertNotNull(members3);
        Assertions.assertEquals(0, members3.size());
    }

    @Test
    @DisplayName("특정 강연의 특정 회원 상태 조회")
    void getMember() {
        String talkId1 = "T00001";
        String mem1 = "test1";
        TalkMem member1 = talkMemFinder.getMember(talkId1, mem1);
        Assertions.assertNotNull(member1);
        Assertions.assertEquals(TalkState.REGISTERATION, member1.getState());

        String mem2 = "test2";
        TalkMem member2 = talkMemFinder.getMember(talkId1, mem2);
        Assertions.assertNotNull(member2);
        Assertions.assertEquals(TalkState.CANCEL, member2.getState());

    }

    @Test
    @DisplayName("신청 내역 조회")
    void getTalkLogs() {
        String mem1 = "test1";
        List<TalkMemberDto> talkLogs = talkMemFinder.getTalkLogs(mem1);
        Assertions.assertNotNull(talkLogs);
        Assertions.assertEquals(2, talkLogs.size());
    }
}