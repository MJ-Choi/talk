package com.kstd.talk.repository;

import com.kstd.talk.config.TestConfig;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.TalkState;
import com.kstd.talk.entity.TalkMem;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

/**
 * 이 테스트는 init.sql 로 초기화 된 상태에서만 정상동작
 */
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TalkTransactionRepository.class, TalkFinder.class, TalkMemFinder.class, TalkIdSeqRepository.class, TestConfig.class})
class TalkTransactionRepositoryTest {
    @Autowired
    private TalkTransactionRepository talkTransactionRepository;
    @Autowired
    private TalkIdSeqRepository talkIdSeqRepository;
    @Autowired
    private TalkFinder talkFinder;
    @Autowired
    private TalkMemFinder talkMemFinder;

    @Test
    @DisplayName("강연 등록")
    void insert() {
        TalkBaseDto baseDto = new TalkBaseDto();
        baseDto.setSpeaker("test-spreaker");
        baseDto.setStartDtm("2025-01-10 10:00");
        baseDto.setPlace("test-place");
        baseDto.setSeat(5);
        baseDto.setDesc("spring test");
        TalkBaseDto result = talkTransactionRepository.insert(baseDto);
        log.info("result: {}", result);
        Assertions.assertFalse(result.getTalkId().isEmpty());
    }

    @Test
    @DisplayName("수강 등록")
    void register() {
        String talkId = "T00001";
        String memId = "TSTM1";
        TalkBaseDto talkInfo = talkFinder.getTalkInfo(talkId);
        log.info("talkInfo: {}", talkInfo);
        Assertions.assertNotNull(talkInfo);
        // 생성된 강의에 멤버 등록
        TalkMemberDto result = talkTransactionRepository.register(talkId, memId);
        Assertions.assertEquals(TalkState.REGISTERATION, result.getState());
    }

    @Test
    @DisplayName("수강 취소")
    void cancel() {
        String talkId = "T00001";
        String memId = "TSTM1";
        TalkBaseDto talkInfo = talkFinder.getTalkInfo(talkId);
        Assertions.assertNotNull(talkInfo);
        TalkMemberDto register = talkTransactionRepository.register(talkId, memId);
        TalkMem member = talkMemFinder.getMember(talkId, memId);
        log.info("member: {}", member);
        Assertions.assertNotNull(member);

        // 수강 취소
        TalkMemberDto result = talkTransactionRepository.cancel(talkId, memId);
        log.info("cancel result: {}", result);
        Assertions.assertEquals(TalkState.CANCEL, result.getState());
        Assertions.assertNotEquals(register.getState(), result.getState());
    }
}