package com.example.deneme.customer;

import com.example.deneme.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PreAuthorize("@authService.isOwnerOrAdmin(#id, authentication)")
    @GetMapping("/customers")
    public List<Customer> listCustomers(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String email){
        return customerService.filterCustomers(id, email);
    }


    @GetMapping("/getinfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Customer> getCustomerInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(user.getCustomer());
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
