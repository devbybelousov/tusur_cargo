package com.tusur.cargo.service;

import com.tusur.cargo.dto.AdminRequest;
import com.tusur.cargo.dto.RecipientMessageRequest;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.User;
import java.util.List;

public interface UserService {

  User getUserInfo(Long id);

  List<User> getAllUser();

  short editEmail(String email, Long id);

  short verifyEmail(String token);

  short editName (String name, Long id);

  short editPassword (String oldPassword, String newPassword, Long id);

  short createAdmin(AdminRequest adminRequest);

  short deleteUser(Long id);

  short banUser(Long id);

  List<User> getAllUsersByCurrentUser(Long id);

  List<Feedback> getAllUsersFeedback(Long id);

  short createRecipientMessage(RecipientMessageRequest messageRequest);
}
