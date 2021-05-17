package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.FeedbackRequest;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.FeedbackRepository;
import com.tusur.cargo.repository.OrderRepository;
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
  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public short create(FeedbackRequest feedbackRequest) {

    User user = userRepository.findByUserId(feedbackRequest.getUserId())
        .orElseThrow(() -> new NotFoundException(
            "User not found with id - " + feedbackRequest.getUserId()));

    User author = userRepository.findByUserId(feedbackRequest.getAuthorId())
        .orElseThrow(() -> new NotFoundException(
            "User not found with id - " + feedbackRequest.getAuthorId()));

    Order order = orderRepository.findById(feedbackRequest.getOrderId()).orElseThrow(
        () -> new NotFoundException("Order not found with id - " + feedbackRequest.getOrderId()));

    Feedback feedback = Feedback.builder()
        .author(author)
        .order(order)
        .content(feedbackRequest.getContent())
        .rating(feedbackRequest.getRating())
        .created_at(Instant.now())
        .build();

    user.getFeedbackList().add(feedbackRepository.save(feedback));

    double rating = 0;
    for (Feedback unit : user.getFeedbackList()) {
      rating += unit.getRating();
    }
    user.setRating(rating / user.getFeedbackList().size());
    userRepository.save(user);
    return 1;
  }


  @Override
  @Transactional
  public short delete(Long id) {
    Feedback feedback = feedbackRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Feedback not found with id - " + id));
    feedbackRepository.delete(feedback);
    return 1;
  }
}
