package com.example.car.customer.DTO;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CustomerRequestDTO {
    private String username;

    private String password;

    private String fullName;

    @Pattern(regexp = "^5\\d{9}$", message = "Phone number must be 10 digits starting with 5")
    private String phoneNumber;

    @Email(message = "Email must be valid")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must have a valid domain (e.g., .com, .net)"
    )
    private String email;

    @Past
    private LocalDate birthDate;

    @Past
    private LocalDate licenseDate;

    public CustomerRequestDTO() {
    }


    public CustomerRequestDTO(String username, String password, String fullName, String phoneNumber, String email, LocalDate birthDate, LocalDate licenseDate) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.licenseDate = licenseDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(LocalDate licenseDate) {
        this.licenseDate = licenseDate;
    }
}