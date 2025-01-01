package com.kstd.talk.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TalkState {

  REGISTERATION("01","등록"),
  CANCEL("02","취소");

  private final String code;
  private final String desc;

  TalkState(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
