package com.example.deneme.customer;

import com.example.deneme.exception.EmailAlreadyInUseException;
import com.example.deneme.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(CustomerRequestDTO customerRequestDTO){
        Optional<Customer> existing = customerRepository.findByEmailAndNotDeleted(customerRequestDTO.getEmail()); // Find all customers with this email

        // If at least one exists, email is in use
        if (existing.isPresent()){
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }

        // Create new customer with specified fields and save to repository
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        return customerRepository.save(customer);
    }

    public void updateCustomer(UUID id, CustomerRequestDTO customerRequestDTO){
        Optional<Customer> existing = customerRepository.findByEmailAndNotDeleted(customerRequestDTO.getEmail());

        if (existing.isPresent()){
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }

        Customer customer = getCustomerById(id);
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customerRepository.save(customer);
    }

    public void deleteCustomer(UUID id){
        Customer customer = getCustomerById(id); // Find non-deleted customer

        // Perform soft delete
        customer.softDelete();
        customerRepository.save(customer);
    }

    // Try to retrieve the customer by id, if it does not succeed, throw exception
    public Customer getCustomerById(UUID id){
        return customerRepository.findByIdAndNotDeleted(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public List<Customer> getCustomers(){
        return customerRepository.findAllNotDeleted();
    }


}
