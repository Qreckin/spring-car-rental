package com.example.deneme.customer;

import com.example.deneme.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers")
    public List<Customer> listCustomers(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(required = false) Integer licenseYear,
            @RequestParam(required = false) String username) {

        return customerService.filterCustomers(id, email, fullName, phoneNumber, birthDate, licenseYear, username);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public Customer getCustomerInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return customerService.getCustomerInfo(user.getCustomer().getId());
    }



    @PreAuthorize("@authService.isOwnerOrAdmin(#id, authentication)")
    @PutMapping("/customers/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable UUID id, @Valid @RequestBody CustomerRequestDTO updatedCustomer){

        customerService.updateCustomer(id, updatedCustomer);
        return ResponseEntity.ok("Customer with ID: " + id + " has been updated successfully.");
    }

    @PreAuthorize("@authService.isOwnerOrAdmin(#id, authentication)")
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer with ID: " + id + " has been deleted successfully.");
    }
}
