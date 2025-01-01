package com.kstd.talk.api.error;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCode {
  SUCCESS(200, "정상응답"),
  CHECKED(400,"이미 신청한 강연입니다"),
  INPUT_ERROR(400, "잘못된 입력값입니다."),
  NO_SEAT(400, "잔여 좌석이 없습니다."),
  NO_DATA(404, "요청한 데이터가 없습니다.");

  private int code;
  private String msg;

  ErrorCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
