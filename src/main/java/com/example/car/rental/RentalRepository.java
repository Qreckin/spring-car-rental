package com.example.car.rental;

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

    @Query("SELECT r FROM Rental r WHERE r.id = :id AND r.deletedAt IS NULL")
    Optional<Rental> findByIdAndNotDeleted(@Param("id") UUID id);

    @Query("SELECT r FROM Rental r WHERE " +
            "r.deletedAt IS NULL AND " +
            "(:carId IS NULL OR r.car.id = :carId) AND " +
            "(:status IS NULL OR r.status = :status) AND " +
            "(CAST(:startDate AS timestamp) IS NULL OR r.rentalStartDate >= :startDate) AND " +  // Minimum date is startDate
            "(CAST(:endDate AS timestamp) IS NULL OR r.rentalEndDate <= :endDate)")  // Maximum date is endDate
    List<Rental> filterRentals(
            @Param("customerId") UUID customerId,
            @Param("carId") UUID carId,
            @Param("status") Rental.Status status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT r FROM Rental r WHERE " +
            "r.deletedAt IS NULL AND " +
            "r.car.id = :carId AND " +
            "r.status IN :statuses AND " +
            "r.rentalStartDate < :endDate AND " +
            "r.rentalEndDate > :startDate")
    List<Rental> findOverlappingRentals(
            @Param("carId") UUID carId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("statuses") List<Rental.Status> statuses);


    @Query("SELECT COUNT(r) > 0 FROM Rental r WHERE r.id = :rentalId AND r.customer.user.username = :username AND r.deletedAt IS NULL")
    boolean isRentalOwnedByUser(@Param("rentalId") UUID rentalId, @Param("username") String username);

}
