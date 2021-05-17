package com.tusur.cargo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBlackListRequest {

  @NotNull
  @ApiModelProperty(notes = "Идентификатор пользователя",
      example = "2", required = true)
  private Long id;

  @NotBlank
  @ApiModelProperty(notes = "Причина блокировки",
      example = "Причина", required = true)
  private String message;

  @JsonFormat(pattern = "yyyy-mm-dd", timezone = "UTC")
  @ApiModelProperty(notes = "Дата разблокировки",
      example = "2022-05-22", required = false)
  private Date unlockDate;
}
