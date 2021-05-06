package com.tusur.cargo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @NotBlank(message = "Email is required")
  @Email
  private String email;

  @JsonIgnore
  @NotBlank(message = "Password is required")
  private String password;

  @NotBlank(message = "Name is required")
  private String name;

  @JsonFormat(shape = Shape.STRING)
  @OneToOne(cascade = CascadeType.MERGE)
  private Role role;

  @JsonIgnore
  @OneToMany
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private List<Order> orders;

  @JsonIgnore
  private Boolean enabled;

  @JsonIgnore
  private Instant deleted_at;

  @JsonIgnore
  private Boolean isNonLocked;
  private double rating;

  @OneToMany
  @JsonIgnore
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private List<Interlocutor> interlocutors;

  @OneToMany
  @JsonIgnore
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private List<Feedback> feedbackList;

  public User(String email, String password, String name, boolean enabled) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.enabled = enabled;
  }

  public User(Long id, String email, String password, String name, boolean enabled) {
    this.userId = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "User(" + getUserId() + getEmail() + getName() + getEnabled() + getRole() + ")";
  }
}
