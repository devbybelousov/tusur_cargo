package com.tusur.cargo.security;

import com.tusur.cargo.exception.NotFoundException;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("No user Found with email: " + email));

    log.info("user with email: {} successfully loaded", user.getEmail());
    return UserPrincipal.create(user);
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new NotFoundException("User with id = " + id + " not found")
    );
    return UserPrincipal.create(user);
  }
}
