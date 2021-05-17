package com.tusur.cargo.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterlocutorRequest {

  @NotEmpty
  @ApiModelProperty(notes = "Идентификатор пользователя",
      example = "1", required = true)
  private Long userId;

  @NotEmpty
  @ApiModelProperty(notes = "Идентификатор объявления",
      example = "1", required = true)
  private Long orderId;

  @NotEmpty
  @ApiModelProperty(notes = "Идентификатор собеседника",
      example = "1", required = true)
  private Long interlocutorId;
}
