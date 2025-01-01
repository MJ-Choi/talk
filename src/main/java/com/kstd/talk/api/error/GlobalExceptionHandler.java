package com.kstd.talk.api.error;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * API 응답 시, 사용자 에러 반환
   *
   * @param ex
   * @return
   */
  @ExceptionHandler(ResponseException.class)
  public ResponseEntity<Map<String, String>> handleApiException(ResponseException ex) {
    return ResponseEntity.ok(ex.getResult());
  }
}
