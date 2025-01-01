package com.kstd.talk.service;

import com.kstd.talk.config.TestConfig;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.dto.request.TalkRequest;
import com.kstd.talk.entity.QTalkList;
import com.kstd.talk.entity.QTalkMem;
import com.kstd.talk.repository.TalkFinder;
import com.kstd.talk.repository.TalkIdSeqRepository;
import com.kstd.talk.repository.TalkMemFinder;
import com.kstd.talk.repository.TalkTransactionRepository;
import com.kstd.talk.util.DateUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 이 테스트는 init.sql 로 초기화 된 상태에서만 정상동작
 */
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TalkMgmtService.class, TalkFinder.class,
        TestConfig.class, TalkMemFinder.class, TalkTransactionRepository.class, TalkIdSeqRepository.class})
class TalkMgmtServiceTest {
    @Autowired
    private TalkMgmtService talkMgmtService;
    @Autowired
    private TalkFinder talkFinder;
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
    @DisplayName("강연 목록 조회")
    void getList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TalkBaseDto> page = talkMgmtService.getList(pageable);
        List<TalkBaseDto> list = page.stream().toList();
        log.info("page:{}: {}", list.size(), list);
        Assertions.assertTrue(list.size() >= 3);
    }

    @Test
    @DisplayName("강연 등록 테스트")
    void register() {
        TalkRequest talkRequest = new TalkRequest();
        talkRequest.setSpeaker("tcode");
        talkRequest.setPlace("intelliJ");
        talkRequest.setSeat(3);
        talkRequest.setStartDtm("2025-01-10 14:30");
        talkRequest.setDesc("테스트 코드에서 등록 테스트");
        TalkBaseDto register = talkMgmtService.register(talkRequest);
        Assertions.assertNotNull(register);
        Assertions.assertTrue(register.getTalkId().length() > 0);
    }

    @Test
    void getTalkMembers() {
        registerMember();

        Pageable pageable = PageRequest.of(0, 3);
        Page<TalkBaseDto> talkMembers = talkMgmtService.getTalkMembers(pageable);
        List<TalkBaseDto> list = talkMembers.get().collect(Collectors.toList());

        log.info("talkInfoList:{}: {}", list.size(), list);
        Assertions.assertEquals(3, list.size());
    }

    @Test
    @DisplayName("강연 정보 조회")
    void getTalkMembersByTalkId() {
        registerMember();

        String talkId = "T00003";
        TalkBaseDto talkInfo = talkMgmtService.getTalkMembers(talkId);

        log.info("talkInfo: {}", talkInfo);
        Assertions.assertEquals(2, talkInfo.getMembers().size());
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
                        "T00003", "test2", TalkState.CANCEL, DateUtil.strToDt("2024-12-30 11:00"), DateUtil.strToDt("2024-12-30 11:05")
                )
                .execute();

        factory.insert(qTalkMem)
                .columns(
                        qTalkMem.talkId, qTalkMem.memId, qTalkMem.state, qTalkMem.dtm, qTalkMem.updateDtm
                )
                .values(
                        "T00003", "test3", TalkState.REGISTERATION, DateUtil.strToDt("2024-12-31 11:00"), DateUtil.strToDt("2024-12-31 11:00")
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