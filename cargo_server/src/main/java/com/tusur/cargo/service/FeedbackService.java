package com.tusur.cargo.service;

import com.tusur.cargo.dto.FeedbackRequest;
import com.tusur.cargo.model.Feedback;
import java.util.List;

public interface FeedbackService {

  short create(FeedbackRequest feedbackRequest);

  short delete(Long id);
}
