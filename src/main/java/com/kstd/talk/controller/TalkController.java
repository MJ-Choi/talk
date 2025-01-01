package com.kstd.talk.controller;

import com.kstd.talk.api.ResponseDto;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.TalkMemberDto;
import com.kstd.talk.dto.request.TalkMemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "front", description = "강연 수강자가 사용하는 API")
public interface TalkController {

    @Operation(
            summary = "강연 목록 조회",
            description = "조회일을 기준으로 신청 가능한 강연목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<Page<TalkBaseDto>> getList(@PageableDefault(page = 0, size = 10) Pageable pageable);

    @Operation(
            summary = "강연 신청",
            description = "조회일을 기준으로 신청 가능한 강연목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<TalkMemberDto> register(@RequestBody TalkMemberRequest request);

    @Operation(
            summary = "강연 신청 목록 조회",
            description = "조회일을 기준으로 신청 가능한 강연목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<List<TalkMemberDto>> getPopularList(@PathVariable String id);

    @Operation(
            summary = "강연 취소",
            description = "조회일을 기준으로 신청 가능한 강연목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<TalkMemberDto> cancel(@RequestBody TalkMemberRequest request);

    @Operation(
            summary = "인기 강연 목록 조회",
            description = "조회일을 기준으로 신청 가능한 강연목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<Page<TalkBaseDto>> getPopularList(@PageableDefault(size = 10, page = 0) Pageable pageable);
}
