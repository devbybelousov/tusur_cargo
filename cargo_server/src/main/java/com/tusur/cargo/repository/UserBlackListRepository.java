package com.tusur.cargo.repository;

import com.tusur.cargo.model.UserBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlackListRepository extends JpaRepository<UserBlackList, Long> {

}
