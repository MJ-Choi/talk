package com.kstd.talk.controller;

import com.kstd.talk.api.ResponseDto;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.request.TalkRequest;
import com.kstd.talk.service.TalkMgmtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/talk/mgmt")
@AllArgsConstructor
public class TalkMgmtControllerImpl implements TalkMgmtController {

    private final TalkMgmtService talkService;

    @Override
    @GetMapping(value = "")
    public ResponseDto<Page<TalkBaseDto>> getList(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Input: {}", pageable);
        return new ResponseDto<>(talkService.getList(pageable));
    }

    @Override
    @PostMapping(value = "/register")
    public ResponseDto<TalkBaseDto> register(@RequestBody TalkRequest request) {
        log.info("Input: {}", request);
        return new ResponseDto<>(talkService.register(request));
    }

    @Override
    @GetMapping(value = "/list")
    public ResponseDto<Page<TalkBaseDto>> getListWithMember(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Input: {}", pageable);
        return new ResponseDto<>(talkService.getTalkMembers(pageable));
    }

    @Override
    @GetMapping(value = "/list/{talkId}")
    public ResponseDto<TalkBaseDto> getListWithMember(@PathVariable String talkId) {
        log.info("Input: {}", talkId);
        return new ResponseDto<>(talkService.getTalkMembers(talkId));
    }

}