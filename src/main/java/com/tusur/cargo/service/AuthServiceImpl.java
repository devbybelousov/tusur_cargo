package com.tusur.cargo.service;

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
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
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
      return -1;
    }

    if(!roleRepository.existsByTitle(signupRequest.getRole())){
      Role newRole = new Role();
      newRole.setTitle(signupRequest.getRole());
      roleRepository.save(newRole);
    }

    User user = new User();
    Role role = roleRepository.findByTitle(signupRequest.getRole()).orElseThrow(
        () -> new SpringCargoException("Role not found with title - " + signupRequest.getRole()));
    user.setRole(role);
    user.setEmail(signupRequest.getEmail());
    user.setName(signupRequest.getName());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setEnabled(false);

    String token = generateVerificationToken(userRepository.save(user));
    mailService
        .sendMail(new NotificationEmail("Please Activate Your Account",
            user.getEmail(),
            "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account: " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    return 1;
  }

  @Override
  public String generateVerificationToken(User user) {
    String token = UUID.randomUUID().toString().replace('-', ')');
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setToken(token);
    verificationToken.setUser(user);

    verificationTokenRepository.save(verificationToken);
    return token;
  }

  @Override
  public short verifyAccount(String token) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
        .orElseThrow(() -> new SpringCargoException("Invalid token."));
    return fetchUserAndEnable(verificationToken);
  }

  @Override
  public AuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
            loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authenticate);
    User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
        () -> new SpringCargoException("User not found with email - " + loginRequest.getEmail()));
    if (!user.getEnabled()) {
      return null;
    }
    String token = jwtTokenProvider.generateToken(authenticate);
    return new AuthenticationResponse(user.getUserId(), token, user.getRole().getTitle());
  }

  @Transactional
  public short fetchUserAndEnable(VerificationToken verificationToken) {
    String email = verificationToken.getUser().getEmail();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new SpringCargoException("User not found with email - " + email));
    user.setEnabled(true);
    userRepository.save(user);
    return 1;
  }


}
