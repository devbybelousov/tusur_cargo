package com.tusur.cargo.controller;

import com.tusur.cargo.service.ImageService;
import com.tusur.cargo.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class FileController {

  private final ImageService imageService;
  private final StorageService storageService;

  @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {
      "multipart/form-data"})
  public ResponseEntity<?> uploadImages(@RequestParam("files") MultipartFile[] files) {
    return ResponseEntity.status(HttpStatus.CREATED).body(imageService.uploadFiles(files));
  }

  @GetMapping("/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

    Resource resource = storageService.loadAsResource(filename);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
