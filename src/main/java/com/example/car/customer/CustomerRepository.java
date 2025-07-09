package com.example.car.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Customer> findByIdAndNotDeleted(@Param("id") UUID id);

    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.deletedAt IS NULL")
    Optional<Customer> findByEmailAndNotDeleted(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.user.username = :username AND c.deletedAt IS NULL")
    Optional<Customer> findByUsernameAndNotDeleted(@Param("username") String username);

    @Query("SELECT c FROM Customer c WHERE c.deletedAt IS NULL")
    List<Customer> findAllNotDeleted();

    @Query("""
    SELECT c FROM Customer c
    LEFT JOIN c.user u
    WHERE (:id IS NULL OR c.id = :id)
      AND (:email IS NULL OR c.email = :email)
      AND (:fullName IS NULL OR c.fullName = :fullName)
      AND (:phoneNumber IS NULL OR c.phoneNumber = :phoneNumber)
      AND (:birthDate IS NULL OR c.birthDate = :birthDate)
      AND (:licenseDate IS NULL OR c.licenseDate = :licenseDate)
      AND (:username IS NULL OR u.username = :username)
""")
    List<Customer> filterCustomers(
            @Param("id") UUID id,
            @Param("email") String email,
            @Param("fullName") String fullName,
            @Param("phoneNumber") String phoneNumber,
            @Param("birthDate") LocalDate birthDate,
            @Param("licenseDate") LocalDate licenseDate,
            @Param("username") String username
    );

    boolean existsByEmail(String email);
}
