package com.kstd.talk.service;

import com.kstd.talk.config.FixedTimeConfig;
import com.kstd.talk.config.TestConfig;
import com.kstd.talk.config.TalkMemberConfig;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.dto.request.TalkMemberRequest;
import com.kstd.talk.entity.QTalkMem;
import com.kstd.talk.repository.TalkFinder;
import com.kstd.talk.repository.TalkIdSeqRepository;
import com.kstd.talk.repository.TalkMemFinder;
import com.kstd.talk.repository.TalkTransactionRepository;
import com.kstd.talk.util.DateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 이 테스트는 init.sql 로 초기화 된 상태에서만 정상동작
 */
@Slf4j
@DataJpaTest
@EnableAspectJAutoProxy
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TalkService.class, TestConfig.class, FixedTimeConfig.class,
        TalkFinder.class, TalkMemFinder.class, TalkTransactionRepository.class, TalkMemberConfig.class, TalkIdSeqRepository.class})
class TalkServiceTest {
    @Autowired
    private TalkService talkService;
    @Autowired
    private TalkFinder talkFinder;
    @Autowired
    private Clock clock;
    @Autowired
    private JPAQueryFactory factory;

    @BeforeEach
    void init() {
        // LocalDateTime.now() 모킹
        ReflectionTestUtils.setField(talkService, "clock", clock);
        // 강연 확인
        // T00001 2025-01-05 10:00
        // T00002 2025-01-03 10:00
        // T00003 2024-12-31 10:00
        Assertions.assertNotNull(talkFinder.getTalkInfo("T00001"));
        Assertions.assertNotNull(talkFinder.getTalkInfo("T00002"));
        Assertions.assertNotNull(talkFinder.getTalkInfo("T00003"));
    }

    @Test
    @DisplayName("강연 신청 가능한 페이지 목록 조회")
    void getList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TalkBaseDto> page = talkService.getList(pageable);
        List<TalkBaseDto> talkList = page.get().collect(Collectors.toList());

        log.info("talkList:{}: {}", talkList.size(), talkList);
        Assertions.assertNotNull(talkList);
        Assertions.assertEquals(2, talkList.size());
    }

    @Test
    @DisplayName("강연 신청")
    void register() {
        TalkMemberRequest request = new TalkMemberRequest("T00001", "TST01");
        TalkMemberDto memberDto = talkService.register(request);
        Assertions.assertNotNull(memberDto);
        Assertions.assertEquals(TalkState.REGISTERATION, memberDto.getState());
    }

    @Test
    @DisplayName("신청 내역 조회")
    void getTalkLog() {
        String memId = "TST01";
        talkService.register(new TalkMemberRequest("T00001", memId));
        talkService.register(new TalkMemberRequest("T00002", memId));
        talkService.register(new TalkMemberRequest("T00003", memId));

        List<TalkMemberDto> talkLog = talkService.getTalkLog(memId);
        log.info("talkLog:({}): {}", talkLog.size(), talkLog);
        Assertions.assertNotNull(talkLog);
        Assertions.assertEquals(3, talkLog.size());
    }

    @Test
    @DisplayName("강연 취소")
    void cancel() {
        String memId = "TST01";
        String talkId = "T00003";
        TalkMemberDto regDto = talkService.register(new TalkMemberRequest(talkId, memId));
        Assertions.assertEquals(TalkState.REGISTERATION, regDto.getState());

        TalkMemberDto memberDto = talkService.cancel(new TalkMemberRequest(talkId, memId));
        Assertions.assertEquals(TalkState.CANCEL, memberDto.getState());

    }

    @Test
    @DisplayName("인기강연 목록 조회")
    void getPopularList() {
        registerMember();
        Pageable pageable = PageRequest.of(0, 10);
        Page<TalkBaseDto> page = talkService.getPopularList(pageable);
        Assertions.assertNotNull(page);
        List<TalkBaseDto> talkList = page.get().toList();
        log.info("talkList:{}: {}", talkList.size(), talkList);
        Assertions.assertEquals(1, talkList.size());
        Assertions.assertEquals("T00003", talkList.get(0).getTalkId());
    }

    private void registerMember() {
        QTalkMem qTalkMem = QTalkMem.talkMem;

        // T00003 = 2
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm, qTalkMem.updateDtm
                )
                .values(
                        "T00003", "test1", TalkState.REGISTERATION, DateUtil.strToDt("2024-12-30 11:00"), DateUtil.strToDt("2024-12-30 11:00")
                )
                .execute();
        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm, qTalkMem.updateDtm
                )
                .values(
                        "T00003", "test2", TalkState.CANCEL, DateUtil.strToDt("2024-12-30 11:00"), DateUtil.strToDt("2024-12-30 11:00")
                )
                .execute();

        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm, qTalkMem.updateDtm
                )
                .values(
                        "T00003", "test3", TalkState.REGISTERATION, DateUtil.strToDt("2024-12-31 11:00"), DateUtil.strToDt("2024-12-30 11:00")
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