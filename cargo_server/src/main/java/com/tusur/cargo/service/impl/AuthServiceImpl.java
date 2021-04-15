package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.AuthenticationResponse;
import com.tusur.cargo.dto.LoginRequest;
import com.tusur.cargo.dto.NotificationEmail;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.model.VerificationToken;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.repository.VerificationTokenRepository;
import com.tusur.cargo.security.JwtTokenProvider;
import com.tusur.cargo.service.AuthService;
import com.tusur.cargo.service.mail.MailService;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final VerificationTokenRepository verificationTokenRepository;
  private final MailService mailService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final RoleRepository roleRepository;

  @Value("${app.clientHost}")
  private String clientHost;

  /* Регистрация пользователя */
  @Transactional
  @Override
  public short registerUser(SignupRequest signupRequest) {
    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      return -1;
    }

    if (!AuthService.checkPassword(signupRequest.getPassword())) {
      throw new SpringCargoException("Password is incorrect");
    }

    User user = new User();
    Role role = roleRepository.findByTitle("USER")
        .orElseThrow(() -> new SpringCargoException("Role not found"));

    user.setRole(role);
    user.setEmail(signupRequest.getEmail());
    user.setName(signupRequest.getName());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setEnabled(false);
    user.setIsNonLocked(true);

    String token = generateVerificationToken(userRepository.save(user));
    mailService
        .sendMail(new NotificationEmail("Пожалуйста, подтвердите свой аккаунт",
            user.getEmail(),
            "Спасибо, что зарегистрировались на CarGoBob, " +
                "пожалуйста, нажмите на ссылку чтобы подтвердить свой аккаунт: " +
                clientHost +
                "/api/auth/accountVerification/" + token));
    return 1;
  }


  /* Генерация токена подтверждения*/
  @Transactional
  @Override
  public String generateVerificationToken(User user) {
    String token = UUID.randomUUID().toString().replace("-", "");
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setToken(token);
    verificationToken.setUser(user);

    verificationTokenRepository.save(verificationToken);
    return token;
  }

  /*Верификация аккаунта*/
  @Override
  public short verifyAccount(String token) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
        .orElseThrow(() -> new SpringCargoException("Invalid token."));
    return fetchUserAndEnable(verificationToken);
  }

  /* Авторизация */
  @Override
  public AuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
            loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authenticate);
    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(
            () -> new SpringCargoException("User not found with email-" + loginRequest.getEmail()));

    String token = jwtTokenProvider.generateToken(authenticate);
    return new AuthenticationResponse(user.getUserId(), token, user.getRole().getTitle());
  }

  /* Восстановление пароля*/
  @Override
  public short forgotPassword(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new SpringCargoException("User not found with email-" + email));
    String token = generateVerificationToken(user);
    mailService
        .sendMail(new NotificationEmail("Восстановление пароля аккаунта",
            user.getEmail(),
            "Если вы забыли пароль от аккаунта CarGoBob, " +
                "пожалуйста, нажмите на ссылку: " +
                clientHost +
                "/api/auth/forgot/" + token));
    return 1;
  }

  /* Подтверждение смены пароля*/
  @Override
  @Transactional
  public short changePassword(String password, String token) {
    if (!AuthService.checkPassword(password)) {
      return 11;
    }
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
        .orElseThrow(() -> new SpringCargoException("Invalid token."));
    String email = verificationToken.getUser().getEmail();
    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new SpringCargoException("User not found with email-" + email));
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    return 1;
  }


  @Transactional
  public short fetchUserAndEnable(VerificationToken verificationToken) {
    String email = verificationToken.getUser().getEmail();
    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new SpringCargoException("User not found with email-" + email));
    user.setEnabled(true);
    userRepository.save(user);
    return 1;
  }


}
