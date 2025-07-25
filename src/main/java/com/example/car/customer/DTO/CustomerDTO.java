package com.example.car.customer.DTO;

import com.example.car.customer.Customer;
import com.example.car.rental.DTO.RentalDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CustomerDTO {
    private final UUID id;
    private final String fullName;
    private final String phoneNumber;
    private final String email;
    private final LocalDate birthDate;
    private final LocalDate licenseDate;

    private final Integer licenseType;
    private final String username;

    private final List<RentalDTO> rentals;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.fullName = customer.getFullName();
        this.phoneNumber = customer.getPhoneNumber();
        this.email = customer.getEmail();
        this.birthDate = customer.getBirthDate();
        this.licenseDate = customer.getLicenseDate();
        this.licenseType = customer.getLicenseType().getValue();
        this.username = customer.getUser() != null ? customer.getUser().getUsername() : null;
        this.rentals = customer.getRentals()
                .stream()
                .map(RentalDTO::new)
                .toList();
    }

    // Getters only (no setters for immutability if needed)


    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getLicenseDate() {
        return licenseDate;
    }

    public Integer getLicenseType() {
        return licenseType;
    }

    public String getUsername() {
        return username;
    }

    public List<RentalDTO> getRentals() {
        return rentals;
    }
}