package com.tusur.cargo.service;

import com.tusur.cargo.dto.AuthenticationResponse;
import com.tusur.cargo.dto.LoginRequest;
import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.model.User;

public interface AuthService {

  short registerUser(SignupRequest signupRequest);

  String generateVerificationToken(User user);

  short verifyAccount(String token);

  AuthenticationResponse login(LoginRequest loginRequest);
}

