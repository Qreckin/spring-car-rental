package com.example.deneme.customer;

import com.example.deneme.exception.CustomerAlreadyExistsException;
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

    public void addCustomer(CustomerRequestDTO customerRequestDTO){

        Optional<Customer> existing = customerRepository.findByEmail(customerRequestDTO.getEmail());

        if (existing.isPresent()){
            throw new CustomerAlreadyExistsException(customerRequestDTO.getEmail());
        }

        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customerRepository.save(customer);
    }

    public void updateCustomer(UUID id, CustomerRequestDTO customerRequestDTO){
        Optional<Customer> existing = customerRepository.findByEmail(customerRequestDTO.getEmail());

        if (existing.isPresent()){
            throw new CustomerAlreadyExistsException(customerRequestDTO.getEmail());
        }

        Customer customer = getCustomerById(id);
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customerRepository.save(customer);
    }

    public void removeCustomer(UUID id){
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    public Customer getCustomerById(UUID id){
        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

}
