package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.AuthenticationResponse;
import com.tusur.cargo.dto.LoginRequest;
import com.tusur.cargo.dto.NotificationEmail;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.exception.LockedException;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.exception.UserException;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.model.UserBlackList;
import com.tusur.cargo.model.VerificationToken;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.repository.VerificationTokenRepository;
import com.tusur.cargo.security.JwtTokenProvider;
import com.tusur.cargo.service.AuthService;
import com.tusur.cargo.service.mail.MailService;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Transactional
  @Override
  public short registerUser(SignupRequest signupRequest) {
    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      throw new UserException("User already created.");
    }

    if (!AuthService.checkPassword(signupRequest.getPassword())) {
      throw new NotFoundException("Password is incorrect");
    }

    User user = new User();
    Role role = roleRepository.findByTitle("USER")
        .orElseThrow(() -> new NotFoundException("Role not found"));

    user.setRoles(Collections.singleton(role));
    user.setEmail(signupRequest.getEmail());
    user.setName(signupRequest.getName());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setEnabled(false);

    String token = generateVerificationToken(userRepository.save(user));
    mailService
        .sendMail(new NotificationEmail("????????????????????, ?????????????????????? ???????? ??????????????",
            user.getEmail(),
            "??????????????, ?????? ???????????????????????????????????? ???? CarGoBob, " +
                "????????????????????, ?????????????? ???? ???????????? ?????????? ?????????????????????? ???????? ??????????????: " +
                "http://localhost:8080" +
                "/api/auth/accountVerification/" + token));
    return 1;
  }

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

  @Override
  public short verifyAccount(String token) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
        .orElseThrow(() -> new NotFoundException("Invalid token."));
    return fetchUserAndEnable(verificationToken);
  }

  @Override
  public AuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
            loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authenticate);

    User user = userRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(
            () -> new NotFoundException("User not found with email-" + loginRequest.getEmail()));

    for (UserBlackList item : user.getUserBlackLists()){
      if (item.getUnlockDate().after(new Date())){
        throw new LockedException("???? ?????????????? ?????????? ?? ??????????????, ???? ??????????????????????????. ?????????????? ????????????????????: " + item.getMessage());
      }
    }

    String token = jwtTokenProvider.generateToken(authenticate);
    return new AuthenticationResponse(user.getUserId(), token,
        user.getRoles().stream().map(Role::getTitle).collect(
            Collectors.joining()));
  }

  @Override
  public short forgotPassword(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new NotFoundException("User not found with email-" + email));

    for (UserBlackList item : user.getUserBlackLists()){
      if (item.getUnlockDate().after(new Date())){
        throw new LockedException("???? ?????????????? ?????????? ?? ??????????????, ???? ??????????????????????????. ?????????????? ????????????????????: " + item.getMessage());
      }
    }

    String token = generateVerificationToken(user);

    mailService
        .sendMail(new NotificationEmail("???????????????????????????? ???????????? ????????????????",
            user.getEmail(),
            "???????? ???? ???????????? ???????????? ???? ???????????????? CarGoBob, " +
                "????????????????????, ?????????????? ???? ????????????: " +
                "http://localhost:8080" +
                "/api/auth/recovery/" + token));
    return 1;
  }

  @Override
  @Transactional
  public short changePassword(String password, String token) throws PasswordException {
    if (!AuthService.checkPassword(password)) {
      throw new PasswordException("Password is incorrect.");
    }
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
        .orElseThrow(() -> new NotFoundException("Invalid token."));
    String email = verificationToken.getUser().getEmail();
    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new NotFoundException("User not found with email-" + email));
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    return 1;
  }


  @Transactional
  public short fetchUserAndEnable(VerificationToken verificationToken) {
    String email = verificationToken.getUser().getEmail();
    User user = userRepository.findByEmail(email)
        .orElseThrow(
            () -> new NotFoundException("User not found with email-" + email));
    user.setEnabled(true);
    userRepository.save(user);
    return 1;
  }


}
