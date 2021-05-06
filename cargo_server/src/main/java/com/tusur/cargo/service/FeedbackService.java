package com.tusur.cargo.service;

import com.tusur.cargo.dto.FeedbackRequest;

public interface FeedbackService {

  short create(FeedbackRequest feedbackRequest);

  short delete(Long id);
}
