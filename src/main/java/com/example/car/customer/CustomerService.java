package com.example.car.customer;

import com.example.car.CustomResponseEntity;
import com.example.car.customer.DTO.CustomerDTO;
import com.example.car.customer.DTO.CustomerRequestDTO;
import com.example.car.enums.Enums;
import com.example.car.rental.Rental;
import com.example.car.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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


    public ResponseEntity<CustomResponseEntity> updateCustomer(UUID id, CustomerRequestDTO customerRequestDTO) {
        Customer customer = getCustomerById(id);

        if (customer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.CUSTOMER_NOT_FOUND);

        String email = customerRequestDTO.getEmail();
        String username = customerRequestDTO.getUsername();
        String phoneNumber = customerRequestDTO.getPhoneNumber();

        if (email != null){
            Customer existingCustomer = getCustomerByEmail(email);

            // If a different customer with the same email already exists
            if (existingCustomer != null && !existingCustomer.getId().equals(id))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Customer with Email: " + email + " already exists"));
        }

        if (username != null){
            Customer existingCustomer = getCustomerByUsername(username);

            if (existingCustomer != null && !existingCustomer.getId().equals(id))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Customer with Username: " + username + " already exists"));
        }

        if(phoneNumber != null){
            Customer existingCustomer = getCustomerByPhoneNumber(phoneNumber);

            if (existingCustomer != null && !existingCustomer.getId().equals(id))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Customer with Phone Number: " + phoneNumber + " already exists"));
        }

        if (customerRequestDTO.getFullName() != null) {
            customer.setFullName(customerRequestDTO.getFullName());
        }

        if (isValidPhoneNumber(customerRequestDTO.getPhoneNumber()))
            customer.setPhoneNumber(customerRequestDTO.getPhoneNumber());

        if (customerRequestDTO.getEmail() != null) {
            customer.setEmail(customerRequestDTO.getEmail());
        }

        if (customerRequestDTO.getBirthDate() != null) {
            customer.setBirthDate(customerRequestDTO.getBirthDate());
        }

        if (customerRequestDTO.getLicenseDate() != null) {
            customer.setLicenseDate(customerRequestDTO.getLicenseDate());
        }

        if (customerRequestDTO.getUsername() != null) {
            customer.getUser().setUsername(customerRequestDTO.getUsername());
        }

        if (customerRequestDTO.getPassword() != null) {
            customer.getUser().setPassword(passwordEncoder.encode(customerRequestDTO.getPassword()));
        }

        customerRepository.save(customer);

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Customer has been updated successfully"));
    }

    public ResponseEntity<CustomResponseEntity> deleteCustomer(UUID id){
        Customer customer = getCustomerById(id);

        if (customer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.CUSTOMER_NOT_FOUND);

        Iterator<Rental> iterator = customer.getRentals().iterator();

        while (iterator.hasNext()) {
            Rental rental = iterator.next();

            // Cancel and soft delete rental
            rental.setStatus(Enums.Status.CANCELLED);
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

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Customer has been deleted successfully"));
    }

    public ResponseEntity<CustomResponseEntity> getCustomerInfo(UUID id){
        Customer customer = getCustomerById(id);

        if (customer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.CUSTOMER_NOT_FOUND);

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, new CustomerDTO(customer)));
    }

    // Try to retrieve the customer by id, if it does not succeed, throw exception
    public Customer getCustomerById(UUID id) {
        Optional<Customer> customer = customerRepository.findByIdAndNotDeleted(id);
        if (customer.isPresent())
            return customer.get();
        return null;
    }

    public Customer getCustomerByEmail(String email){
        Optional<Customer> customer = customerRepository.findByEmailAndNotDeleted(email);
        if (customer.isPresent())
            return customer.get();
        return null;
    }

    public Customer getCustomerByUsername(String username){
        Optional<Customer> customer = customerRepository.findByUsernameAndNotDeleted(username);
        if (customer.isPresent())
            return customer.get();
        return null;
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber){
        Optional<Customer> customer = customerRepository.findByPhoneNumberAndNotDeleted(phoneNumber);
        if (customer.isPresent())
            return customer.get();
        return null;
    }


    public ResponseEntity<CustomResponseEntity> filterCustomers(UUID id, String email, String fullName, String phoneNumber, LocalDate birthDate, LocalDate licenseDate, String username) {
        List<Customer> customers = customerRepository.filterCustomers(id, email, fullName, phoneNumber, birthDate, licenseDate, username);
        List<CustomerDTO> customerDTOs = customers.stream().map(CustomerDTO::new).toList(); // Use .collect(Collectors.toList()) if Java < 16

        CustomResponseEntity response = new CustomResponseEntity(CustomResponseEntity.OK, customerDTOs);
        return ResponseEntity.ok(response);
    }

    public void saveCustomer(Customer customer){
        customerRepository.save(customer);
    }


    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\+?[0-9]{10,15}$");
    }

}
