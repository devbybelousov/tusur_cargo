package com.tusur.cargo.service.impl;

import com.tusur.cargo.model.Photo;
import com.tusur.cargo.repository.OrderRepository;
import com.tusur.cargo.repository.PhotoRepository;
import com.tusur.cargo.service.ImageService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

  private final PhotoRepository photoRepository;
  private final FileSystemStorageService storageService;

  @Override
  public List<Long> uploadFiles(MultipartFile[] files) {
    if (files == null) return null;
    return Arrays.stream(files)
        .map(this::uploadImage)
        .collect(Collectors.toList());
  }

  @Transactional
  public Long uploadImage(MultipartFile file) {
    String fileName = storageService.store(file);
    String URL_IMAGE = "http://localhost:8080/api/image/";
    return photoRepository.save(new Photo(URL_IMAGE + fileName)).getPhotoId();
  }
}
