package com.tusur.cargo.dto;

import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.Role;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class InterlocutorResponse {

  private Long userId;

  private String name;

  private Set<Role> role;

  private Order order;
}
