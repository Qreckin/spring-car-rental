package com.example.deneme;

import com.example.deneme.user.User;
import com.example.deneme.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class DenemeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DenemeApplication.class, args);
	}

	@Bean
	public CommandLineRunner setupAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminUsername = "admin";
			String rawPassword = "admin123";

			if (userRepository.findByUsername(adminUsername).isEmpty()) {
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