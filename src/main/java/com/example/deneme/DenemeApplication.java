package com.example.deneme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.deneme")
public class DenemeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DenemeApplication.class, args);
	}

}
