package com.tusur.cargo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CargoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CargoApplication.class, args);
	}

}
