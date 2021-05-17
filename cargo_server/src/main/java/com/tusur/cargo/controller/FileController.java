package com.tusur.cargo.controller;

import com.tusur.cargo.service.ImageService;
import com.tusur.cargo.service.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/image")
@AllArgsConstructor
@Api(value = "image", description = "API для операций с изображениями", tags = "Image API")
public class FileController {

  private final ImageService imageService;
  private final StorageService storageService;

  @ApiOperation(value = "Загрузить изображение на сервер")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK"),
      @ApiResponse(code = 401, message = "Не авторизированный")
  })
  @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {
      "multipart/form-data"})
  public ResponseEntity<?> uploadImages(@ApiParam("Список изображений") @RequestParam("files") MultipartFile[] files) {
    return ResponseEntity.status(HttpStatus.CREATED).body(imageService.uploadFiles(files));
  }

  @ApiOperation(value = "Получить изображение")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK")
  })
  @GetMapping("/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> downloadFile(@ApiParam("Название файла") @PathVariable String filename) {

    Resource resource = storageService.loadAsResource(filename);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }
}
