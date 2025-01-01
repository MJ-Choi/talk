package com.kstd.talk.entity;

import com.kstd.talk.util.DateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "TALK_LIST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TalkList {

  @Id
  @Column(name = "TALK_ID")
  private String talkId;

  @Column(name = "SPEAKER")
  private String speaker;

  @Column(name = "START_DTM")
  private LocalDateTime startDtm;

  @Column(name = "UPDATE_DTM", insertable = false, updatable = false,
      columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  private LocalDateTime updateDtm;

  @Column(name = "PLACE")
  private String place;

  @Column(name = "SEAT")
  private int seat;

  @Column(name = "TALK_DESC")
  private String talkDesc;

  public TalkList(String speaker, LocalDateTime startDtm, String place, int seat, String talkDesc) {
    this.speaker = speaker;
    this.startDtm = startDtm;
    this.place = place;
    this.seat = seat;
    this.talkDesc = talkDesc;
  }

  // 테스트용 생성자
  public TalkList(String talkId, String speaker, String startDtm, String place, int seat, String talkDesc) {
    this.talkId = talkId;
    this.speaker = speaker;
    this.startDtm = DateUtil.strToDt(startDtm);
    this.place = place;
    this.seat = seat;
    this.talkDesc = talkDesc;
  }
}
