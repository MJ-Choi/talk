package com.kstd.talk.controller;


import com.kstd.talk.api.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@Tag(name = "test", description = "swagger test")
public class TestController {

    @Operation(
            summary = "테스트",
            description = "test"
    )
    @ApiResponse(
            responseCode = "200", description = "successful operation"
    )
    @GetMapping(value = "/{str}")
    public ResponseDto<String> getList(@PathVariable String str) {
        return new ResponseDto<>("test:".concat(str));
    }
}
