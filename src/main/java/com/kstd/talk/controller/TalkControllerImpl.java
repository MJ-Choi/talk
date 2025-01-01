package com.kstd.talk.controller;

import com.kstd.talk.api.ResponseDto;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.request.TalkMemberRequest;
import com.kstd.talk.service.TalkService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * front api
 */
@Slf4j
@RestController
@RequestMapping("/talk")
@AllArgsConstructor
public class TalkControllerImpl implements TalkController {

    private final TalkService talkService;

    @Override
    @GetMapping(value = "")
    public ResponseDto<Page<TalkBaseDto>> getList(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Input: {}", pageable);
        return new ResponseDto<>(talkService.getList(pageable));
    }

    @Override
    @PostMapping(value = "/register")
    public ResponseDto<TalkMemberDto> register(@RequestBody TalkMemberRequest request) {
        log.info("Input: {}", request);
        return new ResponseDto<>(talkService.register(request));
    }

    @Override
    @GetMapping(value = "/list/{id}")
    public ResponseDto<List<TalkMemberDto>> getPopularList(@PathVariable String id) {
        log.info("Input: {}", id);
        return new ResponseDto<>(talkService.getTalkLog(id));
    }

    @Override
    @PostMapping(value = "/cancel")
    public ResponseDto<TalkMemberDto> cancel(@RequestBody TalkMemberRequest request) {
        log.info("Input: {}", request);
        return new ResponseDto<>(talkService.cancel(request));
    }

    @Override
    @GetMapping(value = "/popular")
    public ResponseDto<Page<TalkBaseDto>> getPopularList(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        log.info("Input: {}", pageable);
        return new ResponseDto<>(talkService.getPopularList(pageable));
    }

}
