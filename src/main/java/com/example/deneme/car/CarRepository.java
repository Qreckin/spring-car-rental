package com.example.deneme.car;

import com.example.deneme.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarRepository extends JpaRepository<Car, UUID> {

    // JPQL query. If parameter is not given it is NULL so we do not filter by it
    // if its not null, we assert that its field is same with the picked car
    @Query("SELECT c FROM Car c WHERE " +
            "c.deletedAt IS NULL AND " +
            "(:make IS NULL OR c.make = :make) AND " +
            "(:model IS NULL OR c.model = :model) AND " +
            "(:year IS NULL OR c.year = :year) AND " +
            "(:id IS NULL OR c.id = :id)")
    List<Car> filterCars(@Param("make") String make,
                         @Param("model") String model,
                         @Param("year") Integer year,
                         @Param("id") UUID id);

    @Query("SELECT c FROM Car c WHERE c.deletedAt IS NULL AND c.id = :id")
    Optional<Car> findByIdAndNotDeleted(@Param("id") UUID id);

}
