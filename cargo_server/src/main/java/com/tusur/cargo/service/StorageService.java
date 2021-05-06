package com.tusur.cargo.service;

import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String store(MultipartFile file);

  Path load(String filename);

  Resource loadAsResource(String filename);
}
