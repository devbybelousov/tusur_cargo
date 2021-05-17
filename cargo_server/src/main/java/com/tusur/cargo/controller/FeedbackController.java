package com.tusur.cargo.controller;

import com.tusur.cargo.dto.FeedbackRequest;
import com.tusur.cargo.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/feedback")
@PreAuthorize("hasAuthority('USER')")
@Api(value = "feedback", description = "API для операций с отзывами", tags = "Feedback API")
public class FeedbackController {

  private final FeedbackService feedbackService;

  @PostMapping
  @ApiOperation(value = "Создание отзыва")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Создан"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Пользователь или объявление не найдены")
  })
  public ResponseEntity<?> createFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.create(feedbackRequest));
  }

  @DeleteMapping
  @ApiOperation(value = "Удаление отзыва")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный"),
      @ApiResponse(code = 403, message = "Доступ запрещен"),
      @ApiResponse(code = 404, message = "Отзыв не найден")
  })
  public ResponseEntity<?> deleteFeedback(@ApiParam("Идентификатор отзыва") @RequestParam Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(feedbackService.delete(id));
  }

}
