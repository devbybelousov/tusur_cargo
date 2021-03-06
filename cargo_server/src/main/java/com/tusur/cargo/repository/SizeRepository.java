package com.tusur.cargo.repository;

import com.tusur.cargo.model.OrderSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<OrderSize, Long> {

}
