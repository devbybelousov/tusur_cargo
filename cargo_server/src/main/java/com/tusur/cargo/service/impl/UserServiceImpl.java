package com.tusur.cargo.service.impl;

import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import com.tusur.cargo.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public User getUserInfo(Long id) {
    return userRepository.findByUserId(id).orElse(new User());
  }

  @Override
  @Transactional
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  @Transactional
  public short editUser(SignupRequest signupRequest, Long id) {
    User user = userRepository.findByUserId(id).orElse(null);
    if (user == null) return 2;
    Role role = roleRepository.findByTitle(signupRequest.getRole()).orElse(null);
    if (role == null) return 4;
    user.setName(signupRequest.getName());
    user.setEmail(signupRequest.getEmail());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setRole(role);
    userRepository.save(user);
    return 1;
  }

  @Override
  @Transactional
  public short createUser(SignupRequest signupRequest) {
    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      return 3;
    }
    User user = new User(signupRequest.getEmail(),
        passwordEncoder.encode(signupRequest.getPassword()),
        signupRequest.getName(), true);
    Role role = roleRepository.findByTitle(signupRequest.getRole()).orElse(null);
    if (role == null) return 4;
    user.setRole(role);
    userRepository.save(user);
    return 1;
  }

  @Override
  @Transactional
  public short deleteUser(Long id) {
    User user = userRepository.findByUserId(id).orElse(null);
    if (user == null) return 2;
    userRepository.delete(user);
    return 1;
  }

  @Override
  @Transactional
  public short banUser(Long id) {
    User user = userRepository.findByUserId(id).orElse(null);
    if (user == null) return 2;
    user.setIsNonLocked(false);
    userRepository.save(user);
    return 1;
  }

  @Override
  public List<User> getAllUsersByCurrentUser(Long id) {

    return null;
  }
}