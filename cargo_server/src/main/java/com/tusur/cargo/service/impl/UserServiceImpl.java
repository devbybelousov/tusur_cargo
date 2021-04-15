package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.NotificationEmail;
import com.tusur.cargo.dto.RecipientMessageRequest;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.RecipientMessage;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.model.VerificationToken;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.RecipientMessageRepository;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.repository.VerificationTokenRepository;
import com.tusur.cargo.service.AuthService;
import com.tusur.cargo.service.UserService;
import com.tusur.cargo.service.mail.MailService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final RecipientMessageRepository messageRepository;
  private final OrderRepository orderRepository;
  private final AuthService authService;
  private final MailService mailService;
  private final VerificationTokenRepository tokenRepository;
  @Value("${app.clientHost}")
  private String clientHost;


  /* Получение информации о пользователе*/
  @Override
  public User getUserInfo(Long id) {
    return userRepository.findByUserId(id)
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + id));
  }

  /* Получение всех пользователей*/
  @Override
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  /* Редактирование почты*/
  @Override
  public short editEmail(String email, Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + id));
    String token = authService.generateVerificationToken(user);
    mailService
        .sendMail(new NotificationEmail("Пожалуйста, подтвердите новую почту",
            email,
            "Если это вы изменили почту на CarGoBob, " +
                "пожалуйста, нажмите на ссылку чтобы подтвердить новую почту: " +
                clientHost +
                "/api/user/accountVerification/" + token));

    return 1;
  }

  @Override
  @Transactional
  public short verifyEmail(String token) {
    VerificationToken verificationToken = tokenRepository.findByToken(token)
        .orElseThrow(() -> new SpringCargoException("Invalid token."));
    String email = verificationToken.getUser().getEmail();
    User user = userRepository.findByEmail(email)
        .orElse(null);
    if (user == null) {
      log.warn("User not found with email - " + email);
      return 2;
    }
    user.setEnabled(true);
    userRepository.save(user);
    return 1;
  }

  /* Редактирование имени*/
  @Override
  @Transactional
  public short editName(String name, Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + id));
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
            () -> new SpringCargoException("User not found with id-" + id));
    if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
      throw new SpringCargoException("New password is incorrect");
    }
    if (!AuthService.checkPassword(newPassword)) {
      throw new SpringCargoException("New password is incorrect");
    }
    user.setPassword(newPassword);
    userRepository.save(user);
    return 1;
  }

  /* Создание администратора*/
  @Override
  @Transactional
  public short createAdmin(AdminRequest adminRequest) {
    if (userRepository.existsByEmail(adminRequest.getEmail())) {
      return 3;
    }
    User user = new User(adminRequest.getEmail(),
        passwordEncoder.encode(adminRequest.getPassword()),
        "Администратор", true);
    Role role = roleRepository.findByTitle("ADMIN")
        .orElseThrow(() -> new SpringCargoException("Role not found"));
    user.setRole(role);
    userRepository.save(user);
    String token = authService.generateVerificationToken(user);
    mailService
        .sendMail(new NotificationEmail("Пожалуйста, подтвердите свой аккаунт",
            user.getEmail(),
            "Поздравляем Вы новый администратор CarGoBob, " +
                "пожалуйста, нажмите на ссылку чтобы подтвердить свой аккаунт: " +
                clientHost +
                "/api/auth/accountVerification/" + token + "\n Ваш пароль для входа в аккаунт: "
                + adminRequest.getPassword()));
    return 1;
  }


  /* Удаление пользователя */
  @Override
  @Transactional
  public short deleteUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + id));
    userRepository.delete(user);
    return 1;
  }

  /* Блокировка пользователя */
  @Override
  @Transactional
  public short banUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + id));
    user.setIsNonLocked(false);
    userRepository.save(user);
    return 1;
  }

  /* Получение всех пользователей-собеседников */
  @Override
  public List<User> getAllUsersByCurrentUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + id));
    return user.getRecipients().stream().map(RecipientMessage::getRecipient)
        .collect(Collectors.toList());
  }

  /* Получение всех отзывов */
  @Override
  public List<Feedback> getAllUsersFeedback(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + id));
    return user.getFeedbackList();
  }

  /* Добавления пользователю собеседника */
  @Override
  public short createRecipientMessage(RecipientMessageRequest messageRequest) {
    User user = userRepository.findByUserId(messageRequest.getUserId())
        .orElseThrow(
            () -> new SpringCargoException("User not found with id-" + messageRequest.getUserId()));
    User recipient = userRepository.findByUserId(messageRequest.getRecipientId())
        .orElseThrow(() -> new SpringCargoException(
            "User not found with id" + messageRequest.getRecipientId()));
    Order order = orderRepository.findById(messageRequest.getOrderId())
        .orElseThrow(() -> new SpringCargoException(
            "Order not found with id - " + messageRequest.getOrderId()));
    user.getRecipients().add(messageRepository.save(new RecipientMessage(recipient, order)));
    userRepository.save(user);
    return 1;
  }
}
