package com.example.deneme.rental.service;

import com.example.deneme.car.Car;
import com.example.deneme.car.CarService;
import com.example.deneme.customer.Customer;
import com.example.deneme.customer.CustomerService;
import com.example.deneme.exception.*;
import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import com.example.deneme.rental.dto.RentalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {
    private final CarService carService;
    private final CustomerService customerService;
    private final RentalRepository rentalRepository;

    @Autowired
    public RentalService(CarService carService, CustomerService customerService, RentalRepository rentalRepository) {
        this.carService = carService;
        this.customerService = customerService;
        this.rentalRepository = rentalRepository;
    }

    public void addRental(RentalRequestDTO rentalRequestDTO){
        UUID carId = rentalRequestDTO.getCarId();
        UUID customerId = rentalRequestDTO.getCustomerId();
        LocalDateTime start = rentalRequestDTO.getRentalStartDate();
        LocalDateTime end = rentalRequestDTO.getRentalEndDate();

        // If end date is before start date OR start date is before current date, raise exception
        if (end.isBefore(start) || start.isBefore(LocalDateTime.now())){
            throw new IllegalDateTimeException(start, end);
        }

        // Ensure both car and customer exists, if not, throw exception
        Car car = carService.getCarById(carId);
        Customer customer = customerService.getCustomerById(customerId);

        // Check if car is occupied in this time interval
        List<Rental> overlaps = rentalRepository.findOverlappingRentals(carId, start, end);
        if (!overlaps.isEmpty()) {
            throw new CarIsOccupiedException(carId);
        }

        Rental rental = new Rental();

        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setRentalStartDate(start);
        rental.setRentalEndDate(end);
        rental.setStatus(Rental.Status.RESERVED);

        rentalRepository.save(rental);
    }

    public void completeRental(UUID id){
        Optional<Rental> optionalRental = rentalRepository.findByIdAndNotDeleted(id); // Find non-deleted rental

        // If there is none, we do not have the specified rental
        if (optionalRental.isEmpty()){
            throw new RentalNotFoundException(id);
        }

        Rental rental = optionalRental.get(); // Get the rental object itself

        // Rental status must be ACTIVE
        if (rental.getStatus() != Rental.Status.ACTIVE){
            throw new RentalCannotBeCompletedException(id);
        }

        rental.setStatus(Rental.Status.COMPLETED);
        rentalRepository.save(rental);
    }

    public void cancelRental(UUID id){
        Optional<Rental> optionalRental = rentalRepository.findByIdAndNotDeleted(id); // Find non-deleted rental

        // If no rentals exist
        if (optionalRental.isEmpty()){
            throw new RentalNotFoundException(id);
        }

        Rental rental = optionalRental.get();

        // If status is completed or cancelled, we do not cancel them
        if (rental.getStatus() == Rental.Status.COMPLETED || rental.getStatus() == Rental.Status.CANCELLED){
            throw new RentalCannotBeCanceledException(id);
        }

        rental.setStatus(Rental.Status.CANCELLED);
    }


    public List<Rental> filterRentals(UUID customerId, UUID carId, Rental.Status status, LocalDateTime startDate, LocalDateTime endDate){
        return rentalRepository.filterRentals(customerId, carId, status, startDate, endDate);
    }

    public void deleteRental(UUID id) {
        Optional<Rental> optionalRental = rentalRepository.findByIdAndNotDeleted(id);

        if (optionalRental.isEmpty()) {
            throw new RentalNotFoundException(id);
        }

        Rental rental = optionalRental.get();

        // Remove rental from car and customer
        Car car = rental.getCar();
        Customer customer = rental.getCustomer();

        car.getRentals().remove(rental);
        customer.getRentals().remove(rental);

        rental.setStatus(Rental.Status.CANCELLED);
        rental.softDelete();

        // Save all changes
        rentalRepository.save(rental);
        carService.saveCar(car);
        customerService.saveCustomer(customer);
    }

}
