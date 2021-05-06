package com.tusur.cargo.repository;

import com.tusur.cargo.model.Interlocutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterlocutorRepository extends JpaRepository<Interlocutor, Long> {

}
