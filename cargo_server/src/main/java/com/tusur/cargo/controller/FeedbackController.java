package com.tusur.cargo.controller;

import com.tusur.cargo.dto.FeedbackRequest;
import com.tusur.cargo.service.FeedbackService;
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
@RequestMapping("/api/feedback")
@PreAuthorize("hasAuthority('USER')")
public class FeedbackController {

  private final FeedbackService feedbackService;

  @PostMapping
  public ResponseEntity<?> createFeedback(@RequestBody @Valid FeedbackRequest feedbackRequest){
    return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.create(feedbackRequest));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteFeedback(@RequestParam Long id){
    return ResponseEntity.status(HttpStatus.OK).body(feedbackService.delete(id));
  }

}
