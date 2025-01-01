package com.kstd.talk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kstd.talk.util.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@Schema(description = "강연 정보")
public class TalkBaseDto {

    @Schema(description = "강연ID")
    private String talkId;

    @Schema(description = "강연자")
    private String speaker;

    @Schema(description = "강연장")
    private String place;

    @Schema(description = "신청 가능한 좌석수")
    private int seat;

    @Schema(description = "강연 신청 인원")
    private int participants;

    @Schema(description = "강연 신청자의 사번목록")
    private List<String> members;

    @Schema(description = "강연 시작 시간(yyyy-MM-dd HH:mm)")
    private String startDtm;

    @Schema(description = "강연 설명")
    private String desc;

    public TalkBaseDto(
            String talkId, String speaker, String place,
            int seat, Long participants,
            LocalDateTime startDtm,
            String desc) {
        this.talkId = talkId;
        this.speaker = speaker;
        this.place = place;
        this.seat = seat;
        this.participants = participants.intValue();
        this.startDtm = DateUtil.dtToStr(startDtm);
        this.desc = desc;
    }

    public TalkBaseDto(
            String talkId, String speaker, String place,
            int seat,
            LocalDateTime startDtm,
            String desc) {
        this.talkId = talkId;
        this.speaker = speaker;
        this.place = place;
        this.seat = seat;
        this.startDtm = DateUtil.dtToStr(startDtm);
        this.desc = desc;
    }
}
