package com.example.car.customer.DTO;

import com.example.car.validation.NotEmptyIfPresent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CustomerRequestDTO {

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotEmptyIfPresent
    private String fullName;

    @Pattern(regexp = "^5\\d{9}$", message = "Phone number must be 10 digits starting with 5")
    private String phoneNumber;

    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must be valid and contain a proper domain"
    )
    private String email;

    @Past
    private LocalDate birthDate;

    @Past
    private LocalDate licenseDate;

    @Min(value = 0, message = "Gear type must be either 0 or 1")
    @Max(value = 1, message = "Gear type must be either 0 or 1")
    private Integer licenseType;

    public CustomerRequestDTO() {
    }


    public CustomerRequestDTO(String username, String password, String fullName, String phoneNumber, String email, LocalDate birthDate, LocalDate licenseDate, Integer licenseType) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.licenseDate = licenseDate;
        this.licenseType = licenseType;
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

    public Integer getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(Integer licenseType) {
        this.licenseType = licenseType;
    }
}