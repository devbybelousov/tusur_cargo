package com.tusur.cargo.repository;

import com.tusur.cargo.model.Role;
import com.tusur.cargo.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
    JpaSpecificationExecutor<User> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByUserId(Long userId);

  int countByNameLike(String name);
}
