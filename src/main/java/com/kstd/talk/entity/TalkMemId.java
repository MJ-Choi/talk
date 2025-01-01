package com.kstd.talk.entity;

import java.io.Serializable;
import java.util.Objects;

public class TalkMemId implements Serializable {

  private String talkId;
  private String memId;

  // Default constructor
  public TalkMemId() {
  }

  // Parameterized constructor
  public TalkMemId(String talkId, String memId) {
    this.talkId = talkId;
    this.memId = memId;
  }

  // Equals and HashCode methods
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TalkMemId that = (TalkMemId) o;
    return Objects.equals(talkId, that.talkId) && Objects.equals(memId, that.memId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(talkId, memId);
  }

}
