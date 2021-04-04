package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.NotificationEmail;
import com.tusur.cargo.dto.RecipientMessageRequest;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.RecipientMessage;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.RecipientMessageRepository;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.AuthService;
import com.tusur.cargo.service.UserService;
import com.tusur.cargo.service.mail.MailService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Override
  @Transactional
  public User getUserInfo(Long id) {
    return userRepository.findByUserId(id).orElse(new User());
  }

  @Override
  @Transactional
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  @Transactional
  public short editUser(SignupRequest signupRequest, Long id) {
    User user = userRepository.findByUserId(id).orElse(null);
    if (user == null) {
      return 2;
    }
    Role role = roleRepository.findByTitle(signupRequest.getRole()).orElse(null);
    if (role == null) {
      return 4;
    }
    user.setName(signupRequest.getName());
    user.setEmail(signupRequest.getEmail());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setRole(role);
    userRepository.save(user);
    return 1;
  }

  @Override
  @Transactional
  public short createUser(SignupRequest signupRequest) {
    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      return 3;
    }
    User user = new User(signupRequest.getEmail(),
        passwordEncoder.encode(signupRequest.getPassword()),
        signupRequest.getName(), true);
    Role role = roleRepository.findByTitle(signupRequest.getRole()).orElse(null);
    if (role == null) {
      return 4;
    }
    user.setRole(role);
    userRepository.save(user);
    return 1;
  }

  @Override
  @Transactional
  public short createAdmin(AdminRequest adminRequest) {
    if (userRepository.existsByEmail(adminRequest.getEmail())) {
      return 3;
    }
    User user = new User(adminRequest.getEmail(),
        passwordEncoder.encode(adminRequest.getPassword()),
        "Администратор", true);
    Role role = roleRepository.findByTitle("ADMIN").orElse(null);
    if (role == null) {
      return 4;
    }
    user.setRole(role);
    userRepository.save(user);
    String token = authService.generateVerificationToken(user);
    mailService
        .sendMail(new NotificationEmail("Пожалуйста, подтвердите свой аккаунт",
            user.getEmail(),
            "Поздравляем Вы новый администратор CarGoBob, " +
                "пожалуйста, нажмите на ссылку чтобы подтвердить свой аккаунт: " +
                "http://localhost:8080/api/auth/accountVerification/" + token + "\n Ваш пароль для входа в аккаунт: "
                + adminRequest.getPassword()));
    return 1;
  }


  @Override
  @Transactional
  public short deleteUser(Long id) {
    User user = userRepository.findByUserId(id).orElse(null);
    if (user == null) {
      return 2;
    }
    userRepository.delete(user);
    return 1;
  }

  @Override
  @Transactional
  public short banUser(Long id) {
    User user = userRepository.findByUserId(id).orElse(null);
    if (user == null) {
      return 2;
    }
    user.setIsNonLocked(false);
    userRepository.save(user);
    return 1;
  }

  @Override
  public List<User> getAllUsersByCurrentUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(() -> new SpringCargoException("User not found with id - " + id));
    return user.getRecipients().stream().map(RecipientMessage::getRecipient)
        .collect(Collectors.toList());
  }

  @Override
  public List<Feedback> getAllUsersFeedback(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(() -> new SpringCargoException("User not found with id - " + id));
    return user.getFeedbackList();
  }

  @Override
  public short createRecipientMessage(RecipientMessageRequest messageRequest) {
    User user = userRepository.findByUserId(messageRequest.getUserId())
        .orElse(null);
    User recipient = userRepository.findByUserId(messageRequest.getRecipientId())
        .orElse(null);
    if (user == null || recipient == null) {
      return 2;
    }
    Order order = orderRepository.findById(messageRequest.getOrderId()).orElse(null);
    if (order == null) {
      return 9;
    }
    user.getRecipients().add(messageRepository.save(new RecipientMessage(recipient, order)));
    userRepository.save(user);
    return 1;
  }
}
