package com.example.deneme.rental;

import com.example.deneme.car.Car;
import com.example.deneme.car.CarService;
import com.example.deneme.customer.Customer;
import com.example.deneme.customer.CustomerService;
import com.example.deneme.exception.*;
import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import com.example.deneme.rental.RentalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        List<Rental> overlaps = rentalRepository.findOverlappingRentals(carId, start, end, Rental.Status.ACTIVE);
        if (!overlaps.isEmpty()) {
            throw new CarIsOccupiedException(carId);
        }

        Rental rental = new Rental();

        rental.setCar(car);
        car.getRentals().add(rental);

        rental.setCustomer(customer);
        customer.getRentals().add(rental);

        rental.setRentalStartDate(start);
        rental.setRentalEndDate(end);
        rental.setStatus(Rental.Status.RESERVED);

        rentalRepository.save(rental);
    }

    public void activateRental(UUID id){
        Rental rental = findRentalById(id);

        if (rental.getStatus() != Rental.Status.RESERVED){
            throw new RentalCannotBeActivatedException(id);
        }

        rental.setStatus(Rental.Status.ACTIVE);
        rentalRepository.save(rental);
    }

    public void completeRental(UUID id, Integer newKilometer){
        Rental rental = findRentalById(id);

        // Rental status must be ACTIVE
        if (rental.getStatus() != Rental.Status.ACTIVE){
            throw new RentalCannotBeCompletedException(id);
        }

        Car car = rental.getCar();
        if (car != null && newKilometer != null) {
            car.setKilometer(newKilometer);
            carService.saveCar(car);
        }

        rental.setStatus(Rental.Status.COMPLETED);
        rentalRepository.save(rental);
    }

    public void cancelRental(UUID id){
        Rental rental = findRentalById(id);

        // If status is completed or cancelled, we do not cancel them
        if (rental.getStatus() == Rental.Status.COMPLETED || rental.getStatus() == Rental.Status.CANCELLED){
            throw new RentalCannotBeCanceledException(id);
        }

        rental.setStatus(Rental.Status.CANCELLED);
        rentalRepository.save(rental);
    }


    public List<Rental> filterRentals(UUID customerId, UUID carId, Rental.Status status, LocalDateTime startDate, LocalDateTime endDate){
        return rentalRepository.filterRentals(customerId, carId, status, startDate, endDate);
    }

    public void deleteRental(UUID id) {
        Rental rental = findRentalById(id);

        rental.setStatus(Rental.Status.CANCELLED);
        rental.softDelete();

        // Remove rental from car
        Car car = rental.getCar();
        if (car != null){
            car.getRentals().remove(rental);
            rental.setCar(null);
        }

        // Remove rental from customer
        Customer customer = rental.getCustomer();
        if (customer != null) {
            customer.getRentals().remove(rental);
            rental.setCustomer(null);
        }

        rentalRepository.save(rental);
    }

    public Rental findRentalById(UUID id){
        return rentalRepository.findByIdAndNotDeleted(id).orElseThrow(() -> new RentalNotFoundException(id));
    }

}
