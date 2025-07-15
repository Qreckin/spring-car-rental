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

        String newEmail = customerRequestDTO.getEmail();
        String newUsername = customerRequestDTO.getUsername();
        String newPhoneNumber = customerRequestDTO.getPhoneNumber();
        String newFullName = customerRequestDTO.getFullName();
        LocalDate newBirthDate = customerRequestDTO.getBirthDate();
        LocalDate newLicenseDate = customerRequestDTO.getLicenseDate();
        String newPassword = customerRequestDTO.getPassword();

        if (newEmail != null) {
            Customer existingCustomer = getCustomerByEmail(newEmail);
            if (existingCustomer != null && !existingCustomer.getId().equals(id))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Customer with email: " + newEmail + " already exists"));

            if (customer.getEmail().equals(newEmail))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New email cannot be same with the previous email"));

            customer.setEmail(newEmail);
        }

        if (newUsername != null) {
            Customer existingCustomer = getCustomerByUsername(newUsername);
            if (existingCustomer != null && !existingCustomer.getId().equals(id))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Customer with username: " + newUsername + " already exists"));

            if (customer.getUser().getUsername().equals(newUsername))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New username cannot be same with the previous username"));

            customer.getUser().setUsername(newUsername);
        }

        if (newPhoneNumber != null) {
            Customer existingCustomer = getCustomerByPhoneNumber(newPhoneNumber);
            if (existingCustomer != null && !existingCustomer.getId().equals(id))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Customer with phone number: " + newPhoneNumber + " already exists"));

            if (customer.getPhoneNumber().equals(newPhoneNumber))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New phone number cannot be same with the previous phone number"));

            customer.setPhoneNumber(newPhoneNumber);
        }

        if (newFullName != null) {
            if (customer.getFullName().equals(newFullName))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New full name cannot be same with the previous full name"));
            customer.setFullName(newFullName);
        }

        if (newBirthDate != null) {
            if (customer.getBirthDate().equals(newBirthDate))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New birth date cannot be same with the previous birth date"));
            customer.setBirthDate(newBirthDate);
        }

        if (newLicenseDate != null) {
            if (customer.getLicenseDate().equals(newLicenseDate))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New license date cannot be same with the previous license date"));
            customer.setLicenseDate(newLicenseDate);
        }

        if (newPassword != null) {
            if (passwordEncoder.matches(newPassword, customer.getUser().getPassword())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New password cannot be same with the previous password"));
            }
            customer.getUser().setPassword(passwordEncoder.encode(newPassword));
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

}
