package com.kstd.talk.service;

import com.kstd.talk.api.error.ErrorCode;
import com.kstd.talk.api.error.ResponseException;
import com.kstd.talk.config.TalkMemberConfig;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.request.TalkMemberRequest;
import com.kstd.talk.repository.TalkFinder;
import com.kstd.talk.repository.TalkMemFinder;
import com.kstd.talk.repository.TalkTransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TalkService {

    private final TalkMemberConfig config;
    private final Clock clock;

    private final TalkFinder talkFinder;
    private final TalkMemFinder talkMemFinder;
    private final TalkTransactionRepository talkTransactionRepository;

    /**
     * 강연 신청 목록 (/talk)
     *
     * @param pageable 강연 신청 목록 페이지 조건
     * @return 강연 신청 목록
     */
    public Page<TalkBaseDto> getList(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime afterDay = now.minusDays(config.getCloseDay());  // 강연 시작 시간 1일 후 노출 목록에서 노출하지 않는다.
        LocalDateTime maxDays = now.plusDays(config.getOpenDay());    // 강연 시작 시간 1주일 전에 노출
        log.debug("now: {} / between [ {} ]-[ {} ]", now, afterDay, maxDays);

        List<TalkBaseDto> talkList = talkFinder.getPossibleLists(
                afterDay, maxDays, pageable.getOffset(), pageable.getPageSize());

        if (talkList.isEmpty()) {
            return null;
        }
        // 전체 결과 수 계산
        long total = talkList.size();
        return new PageImpl<>(talkList, pageable, total);
    }

    /**
     * 강연 신청
     *
     * @param request
     * @return 신청상태
     */
    public TalkMemberDto register(TalkMemberRequest request) {
        validateMemId(request.getMemId());
        return talkTransactionRepository.register(request.getTalkId(), request.getMemId());
    }

    /**
     * 신청 내역 조회
     *
     * @param memId 사번
     * @return
     */
    public List<TalkMemberDto> getTalkLog(String memId) {
        validateMemId(memId);
        return talkMemFinder.getTalkLogs(memId);
    }

    /**
     * 강연 취소 신청
     *
     * @param request
     * @return
     */
    public TalkMemberDto cancel(TalkMemberRequest request) {
        validateMemId(request.getMemId());
        return talkTransactionRepository.cancel(request.getTalkId(), request.getMemId());
    }

    /**
     * 실시간 인기 강연
     * 3일간 가장 신청이 많은 강연 순(취소는 집계하지 않음)
     *
     * @return
     */
    public Page<TalkBaseDto> getPopularList(Pageable pageable) {
        int limit = config.getPopularDay();

        LocalDateTime maxDay = LocalDateTime.now(clock);
        LocalDateTime minDay = maxDay.minusDays(limit);
        List<TalkBaseDto> lists = talkFinder.getPopularLists(minDay, maxDay, pageable.getOffset(), pageable.getPageSize());

        if (lists.isEmpty()) {
            return null;
        }
        // 전체 결과 수 계산
        long total = lists.size();
        return new PageImpl<>(lists, pageable, total);
    }

    private void validateMemId(String memId) {
        if (memId.length() != config.getIdLength()) {
            log.error("잘못된 사번: {}", memId);
            throw new ResponseException(ErrorCode.INPUT_ERROR);
        }
    }

}
