package com.tusur.cargo.service;

import com.tusur.cargo.dto.InterlocutorRequest;
import com.tusur.cargo.dto.InterlocutorResponse;
import com.tusur.cargo.model.Feedback;
import com.tusur.cargo.model.User;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

  User getUserInfo(Long id);

  List<User> getAllUser(Specification<User> spec, Sort sort);

  short editEmail(String email, Long id);

  short verifyEmail(String token, String newEmail);

  short editName(String name, Long id);

  short editPassword(String oldPassword, String newPassword, Long id);

  short deleteUser(Long id);

  short banUser(Long id);

  List<InterlocutorResponse> getAllInterlocutorByUser(Long id);

  List<Feedback> getAllUsersFeedback(Long id);

  short addInterlocutor(InterlocutorRequest messageRequest);
}
