package com.example.deneme.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, UUID>{
    Optional<Rental> findById(UUID id); // Optional: custom declare explicitly, or rely on JpaRepository
}
