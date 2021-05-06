package com.tusur.cargo;

import com.tusur.cargo.properties.MailProperties;
import com.tusur.cargo.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties({StorageProperties.class, MailProperties.class})
public class CargoApplication {

  public static void main(String[] args) {
    SpringApplication.run(CargoApplication.class, args);
  }

}
