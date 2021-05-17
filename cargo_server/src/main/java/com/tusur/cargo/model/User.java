package com.tusur.cargo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

  @ManyToMany
  @JoinTable(name = "user_role",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "roleId")
  )
  private Set<Role> roles;

  @JsonIgnore
  @ManyToMany
  @JoinTable(name = "user_order",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "orderId")
  )
  private List<Order> orders;

  @JsonIgnore
  private Boolean enabled;

  @JsonIgnore
  private Instant deletedAt;

  private double rating;


  @JsonIgnore
  @ManyToMany
  @JoinTable(name = "user_interlocutor",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "interlocutorId")
  )
  private List<Interlocutor> interlocutors;

  @JsonIgnore
  @ManyToMany
  @JoinTable(name = "user_black_list",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "blackListId")
  )
  private List<UserBlackList> userBlackLists;

  @JsonIgnore
  @ManyToMany
  @JoinTable(name = "user_feedback",
      joinColumns = @JoinColumn(name = "userId"),
      inverseJoinColumns = @JoinColumn(name = "feedbackId")
  )
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
    return "User(" + getUserId() + getEmail() + getName() + getEnabled() + getRoles() + ")";
  }
}
