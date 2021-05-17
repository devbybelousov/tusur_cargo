package com.tusur.cargo.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

  @NotBlank
  @Size(max = 30)
  @Email
  @ApiModelProperty(notes = "Почта пользователя",
      example = "user@example.com", required = true)
  private String email;

  @NotBlank
  @Size(max = 12, min = 6)
  @ApiModelProperty(notes = "Пароль пользователя",
      example = "dfdsff", required = true)
  private String password;
}
