package com.example.deneme.customer;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable UUID id){
        return customerService.getCustomerById(id);
    }

    @GetMapping("/customers")
    public List<Customer> listCustomers(){
        return customerService.getCustomers();
    }

    @PostMapping("/customers")
    public ResponseEntity<String> addCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){

        Customer customer = customerService.addCustomer(customerRequestDTO);
        return ResponseEntity.ok("Customer with ID: " + customer.getId() + " has been created successfully.");
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable UUID id,@Valid @RequestBody CustomerRequestDTO updatedCustomer){

        customerService.updateCustomer(id, updatedCustomer);

        return ResponseEntity.ok("Customer with ID: " + id + " has been updated successfully.");
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id){

        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer with ID: " + id + " has been deleted successfully.");
    }
}
