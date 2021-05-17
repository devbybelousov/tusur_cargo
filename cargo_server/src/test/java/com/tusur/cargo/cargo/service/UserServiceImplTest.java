package com.tusur.cargo.cargo.service;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.tusur.cargo.dto.UserBlackListRequest;
import com.tusur.cargo.enumeration.OrderStatus;
import com.tusur.cargo.exception.PasswordException;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.Interlocutor;
import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.model.VerificationToken;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.InterlocutorRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.repository.VerificationTokenRepository;
import com.tusur.cargo.service.UserService;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private VerificationTokenRepository tokenRepository;

  @MockBean
  private OrderRepository orderRepository;

  @MockBean
  private InterlocutorRepository messageRepository;

  @Before
  public void setUp() {
    User alex = new User().toBuilder()
        .userId(1L)
        .email("alex@example.com")
        .password(passwordEncoder.encode("password"))
        .name("Alex")
        .enabled(true)
        .roles(Collections.singleton(new Role(1L, "USER")))
        .build();

    Mockito.when(userRepository.findByEmail(alex.getEmail()))
        .thenReturn(java.util.Optional.of(alex));
    Mockito.when(userRepository.findByUserId(1L)).thenReturn(java.util.Optional.of(alex));
  }

  @Test
  public void whenValidId_thenUserShouldBeFound() {
    Long id = 1L;
    User found = userService.getUserInfo(id);

    assert found != null;
    assertThat(found.getUserId()).isEqualTo(id);
  }

  @Test
  public void whenValidEmail_thenUserShouldBeFound() {
    String email = "alex@example.com";
    User found = userRepository.findByEmail("alex@example.com").orElse(null);

    assert found != null;
    assertThat(found.getEmail()).isEqualTo(email);
  }

  @Test
  public void whenEditCorrectPassword_oldPasswordCorrect_thenReturnOne() {
    assertEquals(userService.editPassword("password", "newPassword", 1L), 1);
  }

  @Test
  public void whenEditCorrectPassword_oldPasswordInCorrect_thenReturnException() {
    try {
      userService.editPassword("password23", "newPassword", 1L);
      fail();
    } catch (PasswordException e) {
      assertEquals("Old password is incorrect", e.getMessage());
    }
  }

  @Test
  public void whenInCorrectPassword_oldPasswordCorrect_thenReturnException() {
    try {
      userService.editPassword("password", "newPassword№", 1L);
      fail();
    } catch (PasswordException e) {
      assertEquals("New password is incorrect", e.getMessage());
    }
  }

  @Test
  public void whenPasswordAndOldPasswordEquals_thenReturnException() {
    try {
      userService.editPassword("password", "password", 1L);
      fail();
    } catch (PasswordException e) {
      assertEquals("New password is incorrect", e.getMessage());
    }
  }

  @Test
  public void whenDeleteUser_thenReturnOne() {
    assertEquals(userService.deleteUser(1L), 1);
  }

  @Test
  public void whenBanUser_thenReturnOne() {
    UserBlackListRequest userBlackListRequest = new UserBlackListRequest(1L, "message", new Date());
    assertEquals(userService.banUser(userBlackListRequest), 1);
  }

  @Test
  public void whenEditName_thenReturnOne() {
    assertEquals(userService.editName("Bob", 1L), 1);
  }

  @Test
  public void whenEditEmail_thenReturnOne() {
    assertEquals(userService.editEmail("bob@example.com", 1L), 1);
  }

  @Test
  public void whenVerifyEmail_thenReturnOne() {
    User user = new User().toBuilder()
        .userId(1L)
        .email("alex@example.com")
        .password(passwordEncoder.encode("password"))
        .name("Alex")
        .enabled(true)
        .roles(Collections.singleton(new Role(1L, "USER")))
        .build();

    Mockito.when(tokenRepository.findByToken("token"))
        .thenReturn(java.util.Optional.of(new VerificationToken().toBuilder()
            .id(1L)
            .token("token")
            .user(user)
            .build()));

    assertEquals(userService.verifyEmail("token", "bob@example.com"), 1);
  }

  @Test
  public void whenFindAllRecipients_thenReturnRecipients() {
    User user = new User().toBuilder()
        .userId(1L)
        .email("user@example.com")
        .name("User")
        .build();

    User bob = new User().toBuilder()
        .userId(2L)
        .email("bob@example.com")
        .name("Bob")
        .build();

    User mike = new User().toBuilder()
        .userId(3L)
        .email("Mike@example.com")
        .name("Mike")
        .build();

    Order order = new Order().toBuilder()
        .orderId(1L)
        .title("Title")
        .status(OrderStatus.ACTIVE)
        .build();

    List<User> users = Arrays.asList(bob, mike);

    List<Interlocutor> interlocutors = Arrays.asList(new Interlocutor()
        .toBuilder()
        .interlocutorId(1L)
        .order(order)
        .interlocutor(bob)
        .build(), new Interlocutor()
        .toBuilder()
        .interlocutorId(2L)
        .order(order)
        .interlocutor(mike)
        .build());
    user.setInterlocutors(interlocutors);

    Mockito.when(userRepository.findByUserId(1L)).thenReturn(java.util.Optional.of(user));
    assertArrayEquals(userService.getAllInterlocutorByUser(1L).toArray(), users.toArray());
  }

  @Test
  public void whenFindAllFeedback_thenReturnFeedbacks() {
    User user = new User().toBuilder()
        .userId(1L)
        .email("user@example.com")
        .name("User")
        .build();

    User alex = new User().toBuilder()
        .userId(2L)
        .email("alex@example.com")
        .name("Alex")
        .build();

    User bob = new User().toBuilder()
        .userId(3L)
        .email("bob@example.com")
        .name("Bob")
        .build();

    Feedback feedback1 = new Feedback().toBuilder()
        .author(alex)
        .feedbackId(1L)
        .build();

    Feedback feedback2 = new Feedback().toBuilder()
        .author(bob)
        .feedbackId(2L)
        .build();

    List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);
    user.setFeedbackList(feedbackList);

    Mockito.when(userRepository.findByUserId(1L)).thenReturn(java.util.Optional.of(user));
    assertArrayEquals(userService.getAllUsersFeedback(1L).toArray(), feedbackList.toArray());
  }

}
