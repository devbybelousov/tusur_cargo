package com.tusur.cargo.repository;

import com.tusur.cargo.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findByTitle(String title);
  Boolean existsByTitle(String title);
}
