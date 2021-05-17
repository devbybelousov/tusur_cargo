package com.tusur.cargo.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

  @NotBlank
  @Size(min = 6, max = 12)
  @ApiModelProperty(notes = "Старый пароль",
      example = "password", required = true)
  private String oldPassword;

  @NotBlank
  @Size(min = 6, max = 12)
  @ApiModelProperty(notes = "Новый пароль",
      example = "newPassword", required = true)
  private String newPassword;

  @NotEmpty
  @ApiModelProperty(notes = "Идентификатор пользователя",
      example = "1", required = true)
  private Long id;
}
