package com.tusur.cargo.cargo.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tusur.cargo.dto.UserBlackListRequest;
import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.model.User;
import com.tusur.cargo.service.UserService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UserControllerTest {

  private final String REST_URL = "/api/user";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @WithMockUser("user@example.com")
  @Test
  public void findUser_login_statusOk() throws Exception {
    User user = new User(1L, "user@example.com", "password", "User", true);
    when(userService.getUserInfo(1L)).thenReturn(user);

    mockMvc.perform(get(REST_URL + "/info?id=" + 1L))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.userId", is(1)))
        .andExpect(jsonPath("$.email", is("user@example.com")))
        .andExpect(jsonPath("$.name", is("User")));

    verify(userService, times(1)).getUserInfo(1L);
    verifyNoMoreInteractions(userService);
  }

  @WithMockUser("user@example.com")
  @Test
  public void findUser_login_statusNotFound() throws Exception {
    when(userService.getUserInfo(1L)).thenThrow(new NotFoundException(""));

    mockMvc.perform(get(REST_URL + "/info?id=" + 1L))
        .andDo(print())
        .andExpect(status().isNotFound());

    verify(userService, times(1)).getUserInfo(1L);
    verifyNoMoreInteractions(userService);
  }

  @Test
  public void findUser_nologin_statusNotAuthorized() throws Exception {
    mockMvc.perform(get(REST_URL + "/info?id=1"))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @WithMockUser(value = "admin@example.com", authorities = {"SUPER_ADMIN"})
  @Test
  public void findAllUser_authenticated_ifAdmin_statusOk() throws Exception {
    User user = new User(1L, "user@example.com", "password", "User", true);
    ArrayList<User> users = new ArrayList<>();
    users.add(user);
    when(userService.getAllUser(any(), any()))
        .thenReturn(users);
    mockMvc.perform(get(REST_URL))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @WithMockUser(value = "user@example.com")
  @Test
  public void findAllUser_authenticated_ifUser_status403() throws Exception {
    mockMvc.perform(get(REST_URL))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @WithMockUser(value = "admin@example.com", authorities = {"SUPER_ADMIN"})
  @Test
  public void banUser_authenticated_ifAdmin_statusOk() throws Exception {
    UserBlackListRequest userBlackListRequest = new UserBlackListRequest(1L, "message", new Date());
    when(userService.banUser(userBlackListRequest)).thenReturn((short) 1);

    mockMvc.perform(get(REST_URL + "/ban?id=" + 1L))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @WithMockUser(value = "user@example.com")
  @Test
  public void banUser_authenticated_ifUser_statusOk() throws Exception {
    mockMvc.perform(get(REST_URL + "/ban?id=" + 1L))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

}
