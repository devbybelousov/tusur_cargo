package com.tusur.cargo.security;

import com.tusur.cargo.exception.SpringCargoException;
import com.tusur.cargo.model.User;
import com.tusur.cargo.repository.UserRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    Optional<User> userOptional = userRepository.findByEmail(email);
    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("No user Found with email: " + email));
    log.info("user with email: {} successfully loaded", user.getEmail());
    return UserPrincipal.create(user);
  }

  @Transactional(readOnly = true)
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new SpringCargoException("User with id = " + id +" not found")
    );
    return UserPrincipal.create(user);
  }
}
