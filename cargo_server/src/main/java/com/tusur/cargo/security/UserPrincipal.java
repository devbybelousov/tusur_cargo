package com.tusur.cargo.security;

import com.tusur.cargo.model.User;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

  private Long id;
  private String name;
  private String email;
  private String password;
  private boolean enabled;
  private boolean isNonLocked;
  private Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(
      Long id,
      String name,
      String email,
      String password,
      boolean enabled,
      boolean isNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.enabled = enabled;
    this.isNonLocked = isNonLocked;
  }

  public static UserPrincipal create(User user) {
    List<GrantedAuthority> authorityList = Collections
        .singletonList(new SimpleGrantedAuthority(user.getRole().getTitle()));
    return new UserPrincipal(
        user.getUserId(),
        user.getName(),
        user.getEmail(),
        user.getPassword(),
        user.getEnabled(),
        user.getIsNonLocked(),
        authorityList
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
