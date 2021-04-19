package com.tusur.cargo.dto;

import java.time.Instant;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {

  private Long authorId;
  @NotBlank
  @Size(max = 50)
  private String authorName;
  @Max(5)
  @Min(1)
  private double rating;
  @Size(max = 150)
  private String content;
  private Long userId;
  private Long orderId;
}
