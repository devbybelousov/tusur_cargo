package com.tusur.cargo.repository;

import com.tusur.cargo.model.Order;
import com.tusur.cargo.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>,
    JpaSpecificationExecutor<Order> {
  List<Order> findAllByTypeOrderByCreated(String type);
  List<Order> findAllByStatusOrderByCreated(String status);
}
