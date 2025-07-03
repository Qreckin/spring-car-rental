package com.example.deneme.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findById(UUID id); // Optional: custom declare explicitly, or rely on JpaRepository
    Optional<Customer> findByEmail(String email);
}
