package com.kstd.talk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
@Schema(description = "강연을 신청한 회원 정보")
public class TalkMemberDto {

    @Schema(description = "강연ID")
    private String talkId;

    @Schema(description = "사번(5자리)")
    private String memId;

    @Schema(description = "상태 (01. 등록 / 02. 취소)")
    private TalkState state;

    public TalkMemberDto(String talkId, String memId, TalkState state) {
        this.talkId = talkId;
        this.memId = memId;
        this.state = state;
    }
}
