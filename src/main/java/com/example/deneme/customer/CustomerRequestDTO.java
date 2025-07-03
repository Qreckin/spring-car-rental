package com.example.deneme.customer;
import jakarta.validation.constraints.NotBlank;

public class CustomerRequestDTO {

    @NotBlank(message = "Name must not be blank")
    private String name; // Name of the customer

    @NotBlank(message = "Email must not be blank")
    private String email; // Email of the customer

    public CustomerRequestDTO(){

    }

    public CustomerRequestDTO(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
