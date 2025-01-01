package com.kstd.talk.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 직원이 강연을 신청/취소하기 위한 요청서
 * POST /talk/register
 * POST /talk/cancel
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@Schema(description = "강연자 정보")
public class TalkMemberRequest {

  @NotEmpty
  @Schema(description = "강연ID", defaultValue = "T00001")
  private String talkId;
  @NotEmpty
  @Schema(description = "사번(5자리)", defaultValue = "SWG01")
  private String memId;
}
