package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.AdminResponse;
import com.tusur.cargo.dto.NotificationEmail;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.AdminService;
import com.tusur.cargo.service.AuthService;
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
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final AuthService authService;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  /* Создание администратора*/
  @Override
  @Transactional
  public short createAdmin(AdminRequest adminRequest) {
    if (userRepository.existsByEmail(adminRequest.getEmail())) {
      return -1;
    }
    User user = new User(adminRequest.getEmail(),
        passwordEncoder.encode(adminRequest.getPassword()),
        "Администратор", true);
    user.setIsNonLocked(true);
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
                "http://localhost:8080" +
                "/api/auth/accountVerification/" + token + "\n Ваш пароль для входа в аккаунт: "
                + adminRequest.getPassword()));
    return 1;
  }

  @Override
  public List<AdminResponse> getAllAdmin() {
    Role role = roleRepository.findByTitle("ADMIN")
        .orElseThrow(() -> new SpringCargoException("Role ADMIN not found"));

    return userRepository.findAllByRole(role).stream()
        .map(user -> new AdminResponse(
            user.getUserId(),
            user.getName(),
            user.getEmail(),
            0,
            0,
            0))
        .collect(Collectors.toList());
  }
}
