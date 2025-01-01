package com.kstd.talk.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 강연등록 POST /mgmt/talk/register
 */
@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
@Schema(description = "강연 등록 정보")
public class TalkRequest {

  @NotEmpty
  @Schema(description = "강연자", defaultValue = "swagger-speaker")
  private String speaker;
  @NotEmpty
  @Schema(description = "강연장", defaultValue = "swagger-place")
  private String place;
  @Min(1)
  @Schema(description = "좌석수", defaultValue = "10")
  private int seat;
  @NotEmpty
  @Schema(description = "강연시작시간 yyyy-MM-dd HH:mm", defaultValue = "2025-01-10 15:00")
  private String startDtm;
  @NotEmpty
  @Schema(description = "강연 내용", defaultValue = "swagger description")
  private String desc;

}
