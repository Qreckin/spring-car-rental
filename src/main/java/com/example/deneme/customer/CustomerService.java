package com.example.deneme.customer;

import com.example.deneme.car.Car;
import com.example.deneme.car.CarRepository;
import com.example.deneme.exception.EmailAlreadyInUseException;
import com.example.deneme.exception.CustomerNotFoundException;
import com.example.deneme.exception.UsernameInUseException;
import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import com.example.deneme.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void updateCustomer(UUID id, CustomerRequestDTO customerRequestDTO){
        Customer customer = getCustomerById(id);

        if (customerRepository.findByEmailAndNotDeleted(customerRequestDTO.getEmail()).isPresent()){
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }

        if (customerRepository.findByUsernameAndNotDeleted(customerRequestDTO.getUsername()).isPresent()){
            throw new UsernameInUseException(customerRequestDTO.getUsername());
        }

        // Update and save the customer
        customer.setFullName(customerRequestDTO.getFullName());
        customer.setPhoneNumber(customerRequestDTO.getPhoneNumber());
        customer.setEmail(customerRequestDTO.getEmail());

        // Update username and password on the User object
        if (customer.getUser() != null) {
            customer.getUser().setUsername(customerRequestDTO.getUsername());
            customer.getUser().setPassword(passwordEncoder.encode(customerRequestDTO.getPassword()));
        }

        customerRepository.save(customer);
    }

    public void deleteCustomer(UUID id){
        Customer customer = getCustomerById(id); // Find non-deleted customer

        Iterator<Rental> iterator = customer.getRentals().iterator();

        while (iterator.hasNext()) {
            Rental rental = iterator.next();

            // Cancel and soft delete rental
            rental.setStatus(Rental.Status.CANCELLED);
            rental.softDelete();

            // Break link with car
            Car car = rental.getCar();
            if (car != null) {
                car.getRentals().remove(rental); // Remove from car's side
                rental.setCar(null); // Remove from rental's side
            }

            rental.setCustomer(null); // Break from rental's side
            iterator.remove(); // Safe remove from customer list
        }

        User user = customer.getUser();
        if (user != null){
            user.softDelete();
            user.setCustomer(null);
        }

        // Soft delete the customer and save
        customer.setUser(null);
        customer.softDelete();
        customerRepository.save(customer);
    }

    // Try to retrieve the customer by id, if it does not succeed, throw exception
    public Customer getCustomerById(UUID id){
        return customerRepository.findByIdAndNotDeleted(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public List<Customer> filterCustomers(UUID id, String email){
        return customerRepository.filterCustomers(id, email);
    }

    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }


}
