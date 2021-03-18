package com.tusur.cargo.service;

import com.tusur.cargo.dto.SignupRequest;
import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.RoleRepository;
import com.tusur.cargo.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User getUserInfo(Long id) {
    return userRepository.findByUserId(id)
        .orElseThrow(() -> new SpringCargoException("User not found with id - " + id));
  }

  @Override
  public List<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  public short editUser(SignupRequest signupRequest, Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(() -> new SpringCargoException("User not found with name - " + id));
    Role role = roleRepository.findByTitle(signupRequest.getRole()).orElseThrow(
        () -> new SpringCargoException("Role not found with title - " + signupRequest.getRole()));
    user.setName(signupRequest.getName());
    user.setEmail(signupRequest.getEmail());
    user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
    user.setRole(role);
    userRepository.save(user);
    return 1;
  }

  @Override
  public short createUser(SignupRequest signupRequest) {
    if (userRepository.existsByEmail(signupRequest.getEmail())) {
      return -1;
    }
    User user = new User(signupRequest.getEmail(),
        passwordEncoder.encode(signupRequest.getPassword()),
        signupRequest.getName(), true);
    Role role = roleRepository.findByTitle(signupRequest.getRole()).orElseThrow(
        () -> new SpringCargoException("Role not found with title - " + signupRequest.getRole()));
    user.setRole(role);
    userRepository.save(user);
    return 1;
  }

  @Override
  public short deleteUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(() -> new SpringCargoException("User not found with id - " + id));
    userRepository.delete(user);
    return 1;
  }

  @Override
  public short banUser(Long id) {
    User user = userRepository.findByUserId(id)
        .orElseThrow(() -> new SpringCargoException("User not found with id - " + id));
    user.setEnabled(false);
    userRepository.save(user);
    return 1;
  }
}
