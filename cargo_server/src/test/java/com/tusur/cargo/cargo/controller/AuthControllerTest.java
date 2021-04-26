package com.tusur.cargo.cargo.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tusur.cargo.cargo.TestUtil;
import com.tusur.cargo.dto.AuthenticationResponse;
import com.tusur.cargo.dto.LoginRequest;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class AuthControllerTest {

  private final String REST_URL = "/api/auth";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @Test
  public void signIn_statusOk() throws Exception {
    LoginRequest loginRequest = new LoginRequest("user@example.com", "password");

    when(authService.login(any(LoginRequest.class)))
        .thenReturn(new AuthenticationResponse(1L, "token", "USER"));

    mockMvc.perform(post(REST_URL + "/sign-in")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(loginRequest))

    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.userId", is(1)))
        .andExpect(jsonPath("$.accessToken", is("token")))
        .andExpect(jsonPath("$.role", is("USER")));
  }

  @Test
  public void signUp_statusOk() throws Exception {
    SignupRequest signupRequest = new SignupRequest("user@example.com", "password", "User");

    when(authService.registerUser(any(SignupRequest.class)))
        .thenReturn((short) 1);

    mockMvc.perform(post(REST_URL + "/sign-up")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(signupRequest))

    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", is(1)));
  }

  @Test
  public void verifyAccount_statusOk() throws Exception {
    String token = "user@example.com";

    when(authService.verifyAccount(token))
        .thenReturn((short) 1);

    mockMvc.perform(get(REST_URL + "/accountVerification/" + token))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", is(1)));
  }

  @Test
  public void forgotPassword_statusOk() throws Exception {

    String email = "user@example.com";

    when(authService.forgotPassword(email))
        .thenReturn((short) 1);

    mockMvc.perform(get(REST_URL + "/forgot?email=" + email))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", is(1)));
  }

  @Test
  public void recoveryPassword_statusOk() throws Exception {
    String token = "token";
    String password = "password";

    when(authService.changePassword(password, token))
        .thenReturn((short) 1);

    mockMvc.perform(get(REST_URL + "/recovery?password=" + password + "&token=" + token))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", is(1)));
  }

  @Test
  public void recoveryPassword_status400() throws Exception {
    String token = "token";
    String password = "password№";

    when(authService.changePassword(password, token))
        .thenThrow(new PasswordException(""));

    mockMvc.perform(get(REST_URL + "/recovery?password=" + password + "&token=" + token))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}
