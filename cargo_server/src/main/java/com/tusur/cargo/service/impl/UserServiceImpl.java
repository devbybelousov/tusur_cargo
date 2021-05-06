package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.InterlocutorRequest;
import com.tusur.cargo.dto.InterlocutorResponse;
import com.tusur.cargo.dto.NotificationEmail;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.Interlocutor;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import com.tusur.cargo.model.VerificationToken;
import com.tusur.cargo.repository.InterlocutorRepository;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.repository.VerificationTokenRepository;
import com.tusur.cargo.service.AuthService;
import com.tusur.cargo.service.UserService;
import com.tusur.cargo.service.mail.MailService;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final InterlocutorRepository interlocutorRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;
  private final MailService mailService;
  private final VerificationTokenRepository tokenRepository;

  /* Получение информации о пользователе*/
  @Override
  public User getUserInfo(Long id) {
    return userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));
  }

  /* Получение всех пользователей*/
  @Override
  public List<User> getAllUser(Specification<User> spec, Sort sort) {
    return userRepository.findAll(spec, sort);
  }

  /* Редактирование почты*/
  @Override
  public short editEmail(String email, Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));

    String token = authService.generateVerificationToken(user);
    mailService
        .sendMail(new NotificationEmail("Пожалуйста, подтвердите новую почту",
            user.getEmail(),
            "Если это вы изменили почту на CarGoBob, " +
                "пожалуйста, нажмите на ссылку чтобы подтвердить новую почту: " +
                "http://localhost:8080" +
                "/api/user/email/" + email + "/" + token));

    return 1;
  }

  /* Подтверждение почты */
  @Override
  @Transactional
  public short verifyEmail(String token, String newEmail) {
    VerificationToken verificationToken = tokenRepository.findByToken(token)
        .orElseThrow(() -> new NotFoundException("Invalid token."));
    String email = verificationToken.getUser().getEmail();

    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new NotFoundException("User not found with email-" + email));
    user.setEmail(newEmail);

    userRepository.save(user);
    return 1;
  }

  /* Редактирование имени*/
  @Override
  @Transactional
  public short editName(String name, Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));
    user.setName(name);

    userRepository.save(user);
    return 1;
  }

  /* Редактирование пароля*/
  @Override
  @Transactional
  public short editPassword(String oldPassword, String newPassword, Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new PasswordException("Old password is incorrect");
    }

    if (!AuthService.checkPassword(newPassword) || oldPassword
        .equals(newPassword)) {
      throw new PasswordException("New password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    return 1;
  }

  /* Удаление пользователя */
  @Override
  @Transactional
  public short deleteUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));

    user.setDeleted_at(Instant.now());
    userRepository.save(user);
    return 1;
  }

  /* Блокировка пользователя */
  @Override
  @Transactional
  public short banUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));

    user.setIsNonLocked(false);
    userRepository.save(user);
    return 1;
  }

  /* Получение всех собеседников */
  @Override
  public List<InterlocutorResponse> getAllInterlocutorByUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));

    return user.getInterlocutors().stream().map(item -> new InterlocutorResponse()
        .toBuilder()
        .userId(item.getInterlocutor().getUserId())
        .name(item.getInterlocutor().getName())
        .role(item.getInterlocutor().getRole())
        .order(item.getOrder())
        .build())
        .collect(Collectors.toList());
  }

  /* Получение всех отзывов */
  @Override
  public List<Feedback> getAllUsersFeedback(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new NotFoundException("User not found with id-" + id));
    return user.getFeedbackList();
  }

  /* Добавления собеседника */
  @Override
  public short addInterlocutor(InterlocutorRequest interlocutorRequest) {
    User user = userRepository.findByUserId(interlocutorRequest.getUserId())
        .orElseThrow(
            () -> new NotFoundException(
                "User not found with id-" + interlocutorRequest.getUserId()));

    User interlocutor = userRepository.findByUserId(interlocutorRequest.getInterlocutorId())
        .orElseThrow(() -> new NotFoundException(
            "User not found with id" + interlocutorRequest.getInterlocutorId()));

    Order order = orderRepository.findById(interlocutorRequest.getOrderId())
        .orElseThrow(() -> new NotFoundException(
            "Order not found with id - " + interlocutorRequest.getOrderId()));

    user.getInterlocutors().add(interlocutorRepository.save(new Interlocutor(interlocutor, order)));
    userRepository.save(user);
    return 1;
  }
}
