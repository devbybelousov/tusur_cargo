package com.tusur.cargo.cargo.service;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.tusur.cargo.dto.AuthenticationResponse;
import com.tusur.cargo.dto.LoginRequest;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.model.VerificationToken;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.repository.VerificationTokenRepository;
import com.tusur.cargo.security.JwtTokenProvider;
import com.tusur.cargo.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceImplTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private AuthService authService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private VerificationTokenRepository tokenRepository;

  @MockBean
  private AuthenticationManager authenticationManager;

  @Before
  public void setUp() {
    User user = new User().toBuilder()
        .userId(1L)
        .email("user@example.com")
        .password(passwordEncoder.encode("password"))
        .name("User")
        .enabled(true)
        .isNonLocked(true)
        .role(new Role(1L, "USER"))
        .build();

    Mockito.when(userRepository.findByEmail(user.getEmail()))
        .thenReturn(java.util.Optional.of(user));
  }

  @Test
  public void whenRegisterUser_ifUserNotExists_thenReturnOne() {
    String email = "user@example.com";
    Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);

    SignupRequest signupRequest = new SignupRequest(email, "password", "User");

    assertEquals(authService.registerUser(signupRequest), 1);
  }

  @Test
  public void whenRegisterUser_ifUserExists_thenReturnOne() {
    String email = "user@example.com";
    Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);

    SignupRequest signupRequest = new SignupRequest(email, "password", "User");

    assertEquals(authService.registerUser(signupRequest), -1);
  }

  @Test
  public void whenVerifyAccount_thenReturnOne() {
    String token = "token";

    User user = new User().toBuilder()
        .userId(1L)
        .email("user@example.com")
        .password(passwordEncoder.encode("password"))
        .name("User")
        .enabled(true)
        .isNonLocked(true)
        .role(new Role(1L, "USER"))
        .build();

    Mockito.when(tokenRepository.findByToken(token))
        .thenReturn(java.util.Optional
            .ofNullable(
                new VerificationToken().toBuilder().id(1L).user(user).token(token).build()));

    assertEquals(authService.verifyAccount(token), 1);
  }

  @Test
  public void whenLogin_thenReturnOne() {

    LoginRequest loginRequest = new LoginRequest("user@example.com",
        passwordEncoder.encode("password"));
    Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(null);

    assertEquals(authService.login(loginRequest), new AuthenticationResponse(1L, null, "USER"));
  }

  @Test
  public void whenChangeCorrectPassword_thenReturnOne() {
    String token = "token";

    User user = new User().toBuilder()
        .userId(1L)
        .email("user@example.com")
        .password(passwordEncoder.encode("passwordPas1234567890-!@#$%^&*()_+=-<>"))
        .name("User")
        .enabled(true)
        .isNonLocked(true)
        .role(new Role(1L, "USER"))
        .build();

    Mockito.when(tokenRepository.findByToken(token))
        .thenReturn(java.util.Optional
            .ofNullable(
                new VerificationToken().toBuilder().id(1L).user(user).token(token).build()));

    assertEquals(authService.changePassword("newPassword", token), 1);
  }

  @Test
  public void whenChangeInCorrectPassword_thenReturnException() {
    try {
      assertEquals(authService.changePassword("Ajhgfd54445643!#*$&*((&№авпав", "token"), 1);
      fail("Exception not thrown");
    } catch (PasswordException e) {
      assertEquals("Password is incorrect.", e.getMessage());
    }

  }
}
