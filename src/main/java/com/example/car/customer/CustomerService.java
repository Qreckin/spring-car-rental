package com.example.car.customer;

import com.example.car.car.Car;
import com.example.car.exception.EmailAlreadyInUseException;
import com.example.car.exception.CustomerNotFoundException;
import com.example.car.exception.UsernameInUseException;
import com.example.car.rental.Rental;
import com.example.car.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void updateCustomer(UUID id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = getCustomerById(id);

        if (customerRequestDTO.getEmail() != null &&
                customerRepository.findByEmailAndNotDeleted(customerRequestDTO.getEmail())
                        .filter(c -> !c.getId().equals(id))
                        .isPresent()) {
            throw new EmailAlreadyInUseException(customerRequestDTO.getEmail());
        }

        if (customerRequestDTO.getUsername() != null &&
                customerRepository.findByUsernameAndNotDeleted(customerRequestDTO.getUsername())
                        .filter(c -> !c.getId().equals(id))
                        .isPresent()) {
            throw new UsernameInUseException(customerRequestDTO.getUsername());
        }

        if (customerRequestDTO.getFullName() != null) {
            customer.setFullName(customerRequestDTO.getFullName());
        }

        if (customerRequestDTO.getPhoneNumber() != null) {
            customer.setPhoneNumber(customerRequestDTO.getPhoneNumber());
        }

        if (customerRequestDTO.getEmail() != null) {
            customer.setEmail(customerRequestDTO.getEmail());
        }

        if (customerRequestDTO.getBirthDate() != null) {
            customer.setBirthDate(customerRequestDTO.getBirthDate());
        }

        if (customerRequestDTO.getLicenseYear() != null) {
            customer.setLicenseYear(customerRequestDTO.getLicenseYear());
        }

        if (customer.getUser() != null) {
            if (customerRequestDTO.getUsername() != null) {
                customer.getUser().setUsername(customerRequestDTO.getUsername());
            }

            if (customerRequestDTO.getPassword() != null) {
                customer.getUser().setPassword(passwordEncoder.encode(customerRequestDTO.getPassword()));
            }
        }

        customerRepository.save(customer);
    }

    public void deleteCustomer(UUID id){
        Customer customer = getCustomerById(id);

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

    public CustomerDTO getCustomerInfo(UUID id){
        return new CustomerDTO(getCustomerById(id));
    }

    // Try to retrieve the customer by id, if it does not succeed, throw exception
    public Customer getCustomerById(UUID id) {
        return customerRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public CustomerDTO getCustomerDTO(Customer customer){
        return new CustomerDTO(customer);
    }

    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsernameAndNotDeleted(username)
                .orElseThrow(() -> new CustomerNotFoundException(username));
    }

    public List<CustomerDTO> filterCustomers(UUID id, String email, String fullName, String phoneNumber, LocalDate birthDate, Integer licenseYear, String username) {
        List<Customer> customers = customerRepository.filterCustomers(id, email, fullName, phoneNumber, birthDate, licenseYear, username);
        return customers.stream()
                .map(CustomerDTO::new)
                .toList(); // or .collect(Collectors.toList()) if using Java < 16
    }

    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }


}
