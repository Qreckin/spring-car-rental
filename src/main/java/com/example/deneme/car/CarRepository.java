package com.example.deneme.car;

import com.example.deneme.customer.Customer;
import com.example.deneme.rental.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {

    // JPQL query. If parameter is not given it is NULL so we do not filter by it
    // if its not null, we assert that its field is same with the picked car
    @Query("""
    SELECT c FROM Car c 
    WHERE c.deletedAt IS NULL
      AND (:make IS NULL OR c.make = :make)
      AND (:model IS NULL OR c.model = :model)
      AND (:color IS NULL OR c.color = :color)
      AND (:year IS NULL OR c.year = :year)
      AND (:licenseYear IS NULL OR c.requiredLicenseYear <= :licenseYear)
      AND (:minPrice IS NULL OR c.dailyPrice >= :minPrice)
      AND (:maxPrice IS NULL OR c.dailyPrice <= :maxPrice)
      AND (:id IS NULL OR c.id = :id)
      AND NOT EXISTS (
          SELECT r FROM Rental r 
          WHERE r.car = c 
            AND r.deletedAt IS NULL
            AND r.status IN :statuses
            AND (
                (:start < r.rentalEndDate AND :end > r.rentalStartDate)
            )
      )
""")
    List<Car> filterAvailableCars(
            @Param("make") String make,
            @Param("model") String model,
            @Param("color") String color,
            @Param("year") Integer year,
            @Param("licenseYear") Integer licenseYear,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("id") UUID id,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("statuses") List<Rental.Status> statuses
    );

    @Query("SELECT c FROM Car c WHERE c.deletedAt IS NULL AND c.id = :id")
    Optional<Car> findByIdAndNotDeleted(@Param("id") UUID id);

}
