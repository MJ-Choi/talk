package com.kstd.talk.service;

import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.request.TalkRequest;
import com.kstd.talk.repository.TalkFinder;
import com.kstd.talk.repository.TalkMemFinder;
import com.kstd.talk.repository.TalkTransactionRepository;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TalkMgmtService {

  private final TalkFinder talkFinder;
  private final TalkMemFinder talkMemFinder;
  private final TalkTransactionRepository talkTransactionRepository;

  /**
   * 전체 강연 목록 조회
   * @return 강연 정보 목록
   */
  public Page<TalkBaseDto> getList(Pageable pageable) {
    List<TalkBaseDto> talkLists = talkFinder.getList(pageable.getOffset(), pageable.getPageSize());
    if (talkLists.isEmpty()) {
      return null;
    }
    // 전체 결과 수 계산
    long total = talkLists.size();
    return new PageImpl<>(talkLists, pageable, total);
  }

  /**
   * 강연 등록
   * @param request 사용자가 입력한 강연 정보
   * @return 강연ID
   */
  public TalkBaseDto register(TalkRequest request) {
    TalkBaseDto talkBaseDto = new TalkBaseDto();
    talkBaseDto.setSpeaker(request.getSpeaker());
    talkBaseDto.setPlace(request.getPlace());
    talkBaseDto.setParticipants(request.getSeat());
    talkBaseDto.setStartDtm(request.getStartDtm());
    talkBaseDto.setDesc(request.getDesc());

    return talkTransactionRepository.insert(talkBaseDto);
  }

  /**
   * 강연 신청자 목록 조회 (/talk/mgmt/list)
   * @param pageable
   * @return 강연 신청자 목록
   */
  public Page<TalkBaseDto> getTalkMembers(Pageable pageable) {
    log.info("pageable: {}", pageable);
    List<Tuple> results = talkMemFinder.getMembers(pageable.getOffset(), pageable.getPageSize());
    if (results.isEmpty()) {
      return null;
    }
    // 조회된 데이터를 Map 형태로 변환
    Map<String, List<String>> groupedByTalkId = results.stream()
            .collect(Collectors.groupingBy(
                    tuple ->
                            tuple.get(0, String.class),
                    Collectors.mapping(tuple -> tuple.get(1, String.class),
                            Collectors.toList())
            ));

    // Map 데이터를 DTO 리스트로 변환
    List<TalkBaseDto> talkList = groupedByTalkId.entrySet()
            .stream()
            .map(entry -> {
              TalkBaseDto baseDto = new TalkBaseDto();
              baseDto.setTalkId(entry.getKey());
              baseDto.setMembers(entry.getValue());
              return baseDto;
            })
            .collect(Collectors.toList());

    // 전체 결과 수 계산
    long total = talkList.size();

    return new PageImpl<>(talkList, pageable, total);
  }

  /**
   * 강연 신청자 조회 (/talk/mgmt/list/{talkId})
   * @param talkId 강연ID
   * @return 강연 신청자 목록
   */
  public TalkBaseDto getTalkMembers(String talkId) {
    log.info("talkId: {}", talkId);
    List<String> members = talkMemFinder.getMembers(talkId);
    TalkBaseDto talkBaseDto = new TalkBaseDto();
    talkBaseDto.setTalkId(talkId);
    talkBaseDto.setMembers(members);
    return talkBaseDto;
  }
}
