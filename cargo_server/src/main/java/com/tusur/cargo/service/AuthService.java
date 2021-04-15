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

  short forgotPassword(String email);

  short changePassword(String password, String token);

  static boolean checkPassword(String password) {
    String specialSymbols = "#$%^*+.=?@_!-<>";
    String letter = "abcdefghijklmnoprstquvwxyz";
    String numbers = "0123456789";
    for (char c : password.toCharArray()) {
      if (!letter.contains(String.valueOf(c)) && !numbers.contains(String.valueOf(c))
          && !specialSymbols.contains(String.valueOf(c))) {
        return false;
      }
    }
    return true;
  }
}

