package com.example.car.customer;

import com.example.car.rental.RentalDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CustomerDTO {
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private Integer licenseYear;
    private String username;

    private List<RentalDTO> rentalsDTO;

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.fullName = customer.getFullName();
        this.phoneNumber = customer.getPhoneNumber();
        this.email = customer.getEmail();
        this.birthDate = customer.getBirthDate();
        this.licenseYear = customer.getLicenseYear();
        this.username = customer.getUser() != null ? customer.getUser().getUsername() : null;
        this.rentalsDTO = customer.getRentals()
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

    public Integer getLicenseYear() {
        return licenseYear;
    }

    public String getUsername() {
        return username;
    }

    public List<RentalDTO> getRentalsDTO() {
        return rentalsDTO;
    }
}