package com.example.deneme.rental;

import com.example.deneme.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, UUID>{
    Optional<Rental> findById(UUID id); // Optional: custom declare explicitly, or rely on JpaRepository


    @Query("SELECT r FROM Rental r WHERE " +
            "(:customerId IS NULL OR r.customerId = :customerId) AND " +
            "(:carId IS NULL OR r.carId = :carId) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(CAST(:startDate AS timestamp) IS NULL OR r.rentalStartDate >= :startDate) AND " +  // Minimum date is startDate
            "(CAST(:endDate AS timestamp) IS NULL OR r.rentalEndDate <= :endDate)")  // Maximum date is endDate
    List<Rental> filterRentals(
            @Param("customerId") UUID customerId,
            @Param("carId") UUID carId,
            @Param("status") String status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT r FROM Rental r WHERE " +
            "r.carId = :carId AND " +
            "r.rentalStartDate < :endDate AND " +
            "r.rentalEndDate > :startDate")
    List<Rental> findOverlappingRentals(
            @Param("carId") UUID carId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
