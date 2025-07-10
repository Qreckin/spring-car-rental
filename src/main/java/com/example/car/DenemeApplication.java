package com.example.car;

import com.example.car.user.User;
import com.example.car.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DenemeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DenemeApplication.class, args);
	}

	@Bean
	public CommandLineRunner setupAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder, @Value("${admin.password}") String rawPassword) {
		return args -> {
			String adminUsername = "admin";

			if (userRepository.findByUsernameAndNotDeleted(adminUsername).isEmpty()) {
				User admin = new User();
				admin.setUsername(adminUsername);
				admin.setPassword(passwordEncoder.encode(rawPassword));
				admin.setRole("ADMIN");
				admin.setCustomer(null); // Admin has no associated customer

				userRepository.save(admin);
			}
		};
	}
}

// Add rental için kişi tokenı kullanılacak idye gerek yok