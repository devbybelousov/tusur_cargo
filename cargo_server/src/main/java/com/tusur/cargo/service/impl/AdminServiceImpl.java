package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.NotificationEmail;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.exception.UserException;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.AdminService;
import com.tusur.cargo.service.AuthService;
import com.tusur.cargo.service.mail.MailService;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final AuthService authService;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public short createAdmin(AdminRequest adminRequest) {
    if (userRepository.existsByEmail(adminRequest.getEmail())) {
      throw new UserException("User already created.");
    }
    User user = new User(adminRequest.getEmail(),
        passwordEncoder.encode(adminRequest.getPassword()),
        "Администратор №" + (userRepository.countByNameLike("Администратор №") + 1), true);
    Role role = roleRepository.findByTitle("ADMIN")
        .orElseThrow(() -> new NotFoundException("Role not found"));

    user.setRoles(Collections.singleton(role));
    userRepository.save(user);

    String token = authService.generateVerificationToken(user);
    mailService
        .sendMail(new NotificationEmail("Пожалуйста, подтвердите свой аккаунт",
            user.getEmail(),
            "Поздравляем Вы новый администратор CarGoBob, " +
                "пожалуйста, нажмите на ссылку чтобы подтвердить свой аккаунт: " +
                "http://localhost:8080" +
                "/api/auth/accountVerification/" + token + "\n Ваш пароль для входа в аккаунт: "
                + adminRequest.getPassword()));
    return 1;
  }
}
