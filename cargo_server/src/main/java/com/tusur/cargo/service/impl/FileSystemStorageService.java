package com.tusur.cargo.service.impl;

import com.tusur.cargo.properties.StorageProperties;
import com.tusur.cargo.service.StorageService;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;

  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }

  @Override
  public String store(MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    String newFileName =
        UUID.randomUUID().toString().replace("-", "") + "." + FilenameUtils.getExtension(filename);
    try {
      if (file.isEmpty()) {
        return "6";
      }
      if (filename.contains("..")) {
        return "7";
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, this.rootLocation.resolve(newFileName),
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      return "8";
    }

    return newFileName;
  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        return null;
      }
    } catch (MalformedURLException e) {
      return null;
    }
  }
}
