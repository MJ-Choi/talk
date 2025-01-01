package com.kstd.talk.controller;

import com.kstd.talk.api.ResponseDto;
import com.kstd.talk.dto.TalkBaseDto;
import com.kstd.talk.dto.request.TalkRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Tag(name = "backOffice", description = "강연을 관리하기 위한 API")
public interface TalkMgmtController {
    @Operation(
            summary = "강연 목록 조회",
            description = "전체 강연 목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<Page<TalkBaseDto>> getList(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    );

    @Operation(
            summary = "강연 등록",
            description = "강연자, 강연장, 신청 인원, 강연 시간, 강연 내용 입력"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<TalkBaseDto> register(@RequestBody TalkRequest request);

    @Operation(
            summary = "강연 신청자 목록",
            description = "강연 별 신청한 사용자 목록 전체 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<Page<TalkBaseDto>> getListWithMember(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    );

    @Operation(
            summary = "강연 신청자 목록",
            description = "조회일을 기준으로 신청 가능한 강연목록 조회"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    ResponseDto<TalkBaseDto> getListWithMember(
            @Parameter(description = "강연ID", example = "T00001")
            @PathVariable String talkId
    );
}
