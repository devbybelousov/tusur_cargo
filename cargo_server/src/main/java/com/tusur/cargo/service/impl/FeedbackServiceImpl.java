package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.FeedbackRequest;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.FeedbackRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.FeedbackService;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

  private final FeedbackRepository feedbackRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public short create(FeedbackRequest feedbackRequest) {

    User user = userRepository.findByUserId(feedbackRequest.getUserId()).orElse(null);
    if (user == null) return 2;

    Feedback feedback = Feedback.builder()
        .authorId(feedbackRequest.getAuthorId())
        .authorName(feedbackRequest.getAuthorName())
        .content(feedbackRequest.getContent())
        .rating(feedbackRequest.getRating())
        .created(Instant.now())
        .build();

    user.getFeedbackList().add(feedbackRepository.save(feedback));

    double rating = 0;
    for (Feedback unit : user.getFeedbackList()){
      rating += unit.getRating();
    }
    user.setRating(rating/user.getFeedbackList().size());
    userRepository.save(user);
    return 1;
  }


  @Override
  @Transactional
  public short delete(Long id) {
    Feedback feedback = feedbackRepository.findById(id).orElse(null);
    if (feedback == null) return 10;
    feedbackRepository.delete(feedback);
    return 1;
  }
}
