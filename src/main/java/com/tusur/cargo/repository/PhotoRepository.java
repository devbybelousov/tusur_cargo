package com.tusur.cargo.repository;

import com.tusur.cargo.model.Photo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
  Photo findByPhotoId(Long id);
}
