package com.example.deneme.customer;

import com.example.deneme.car.Car;
import com.example.deneme.car.CarRepository;
import com.example.deneme.exception.EmailAlreadyInUseException;
import com.example.deneme.exception.CustomerNotFoundException;
import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;

    private final CarRepository carRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, RentalRepository rentalRepository, CarRepository carRepository){
        this.customerRepository = customerRepository;
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
    }

    public Customer addCustomer(CustomerRequestDTO customerRequestDTO){

        if (customerRepository.findByEmailAndNotDeleted(customerRequestDTO.getEmail()).isPresent()){
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }

        // Create new customer with specified fields and save to repository
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        return customerRepository.save(customer);
    }

    public void updateCustomer(UUID id, CustomerRequestDTO customerRequestDTO){

        if (customerRepository.findByEmailAndNotDeleted(customerRequestDTO.getEmail()).isPresent()){
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }

        // Update and save the customer
        Customer customer = getCustomerById(id);
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
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

        // Soft delete the customer and save
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
