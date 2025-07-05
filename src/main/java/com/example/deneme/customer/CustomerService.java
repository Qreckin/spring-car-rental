package com.example.deneme.customer;

import com.example.deneme.car.Car;
import com.example.deneme.car.CarRepository;
import com.example.deneme.exception.EmailAlreadyInUseException;
import com.example.deneme.exception.CustomerNotFoundException;
import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        List<Rental> rentals = rentalRepository.filterRentals(customer.getId(), null, null, null, null);

        for (Rental rental : rentals){
            rental.setStatus(Rental.Status.CANCELLED);
            rental.softDelete();

            Car car = rental.getCar();
            if (car != null) {
                car.getRentals().remove(rental);
                rental.setCar(null); // Break reference
            }

            // Break customer reference
            rental.setCustomer(null);
        }

        rentalRepository.saveAll(rentals);
        customer.getRentals().clear();
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

    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }


}
