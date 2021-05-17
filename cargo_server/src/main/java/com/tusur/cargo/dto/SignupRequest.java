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
public class SignupRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Size(max = 40)
  @ApiModelProperty(notes = "Почта пользователя",
      example = "user@example.com", required = true)
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, max = 12)
  @ApiModelProperty(notes = "Пароль пользователя",
      example = "dsD554f!", required = true)
  private String password;

  @NotBlank(message = "Name is required")
  @Size(max = 30)
  @ApiModelProperty(notes = "Имя пользователя",
      example = "Иван", required = true)
  private String name;
}
