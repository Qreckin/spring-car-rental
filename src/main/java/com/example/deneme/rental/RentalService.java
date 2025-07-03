package com.example.deneme.rental;

import com.example.deneme.car.Car;
import com.example.deneme.car.CarService;
import com.example.deneme.customer.Customer;
import com.example.deneme.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        // Ensure both car and customer exists, if not, throw exception
        Car car = carService.getCarById(carId);
        Customer customer = customerService.getCustomerById(customerId);

        // Check if car is occupied in this time interval
        List<Rental> overlaps = rentalRepository.findOverlappingRentals(carId, start, end);
        if (!overlaps.isEmpty()) {
            throw new IllegalStateException("Car is already rented during the selected time.");
        }


        Rental rental = new Rental();

        rental.setCarId(carId);
        rental.setCustomerId(customerId);
        rental.setRentalStartDate(start);
        rental.setRentalEndDate(end);
        rental.setStatus("ACTIVE");


        Rental savedRental = rentalRepository.save(rental);

        if (car.getRentalIds() == null)
            car.setRentalIds(new ArrayList<>());
        car.getRentalIds().add(savedRental.getId());
        carService.saveCar(car);

        if (customer.getRentalIds() == null)
            customer.setRentalIds(new ArrayList<>());
        customer.getRentalIds().add(savedRental.getId());
        customerService.saveCustomer(customer);
    }

    public List<Rental> filterRentals(UUID customerId, UUID carId, String status, LocalDateTime startDate, LocalDateTime endDate){
        return rentalRepository.filterRentals(customerId, carId, status, startDate, endDate);
    }

}
