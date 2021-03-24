package com.tusur.cargo.service;

import com.tusur.cargo.model.Photo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  List<Long> uploadFiles(MultipartFile[] files);
}
