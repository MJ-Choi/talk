package com.kstd.talk.api.error;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException {

  private int code;
  private String contents;

  public ResponseException(ErrorCode errCode) {
    super(errCode.getMsg());
    this.code = errCode.getCode();
    this.contents = errCode.getMsg();
  }

  public Map<String, String> getResult() {
    Map<String, String> map = new HashMap<>();
    map.put("code", String.valueOf(this.code));
    map.put("contents", this.contents);
    return map;
  }
}
