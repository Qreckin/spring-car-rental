package com.example.car.user;

import com.example.car.customer.Customer;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameAndNotDeleted(username);
    }

    public User saveUser(String username, String password, String role, Customer customer) {
        User user = new User(username, password, role);
        user.setCustomer(customer);
        return userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsernameAndNotDeleted(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}