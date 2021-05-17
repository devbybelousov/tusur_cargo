package com.tusur.cargo.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

  @NotEmpty
  @ApiModelProperty(notes = "Идентификатор автора",
      example = "1", required = true)
  private Long authorId;

  @Max(5)
  @Min(1)
  @NotEmpty
  @ApiModelProperty(notes = "Оценка пользователю",
      example = "1", required = true)
  private double rating;

  @Size(max = 150)
  @ApiModelProperty(notes = "Текст отзыва",
      example = "Отзыв", required = false)
  private String content;

  @NotEmpty
  @ApiModelProperty(notes = "Идентификатор пользователя",
      example = "2", required = true)
  private Long userId;

  @NotEmpty
  @ApiModelProperty(notes = "Идентификатор объявления",
      example = "3", required = true)
  private Long orderId;
}
