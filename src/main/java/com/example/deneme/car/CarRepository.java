package com.example.deneme.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {
    Optional<Car> findById(UUID id); // Optional: custom declare explicitly, or rely on JpaRepository

    @Query("SELECT c FROM Car c WHERE " +
            "(:make IS NULL OR LOWER(c.make) = LOWER(:make)) AND " +
            "(:model IS NULL OR LOWER(c.model) = LOWER(:model)) AND " +
            "(:year IS NULL OR c.year = :year)")
    List<Car> filterCars(@Param("make") String make,
                         @Param("model") String model,
                         @Param("year") Integer year);
}
