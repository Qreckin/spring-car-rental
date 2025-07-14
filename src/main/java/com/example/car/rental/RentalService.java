package com.example.car.rental;

import com.example.car.CustomResponseEntity;
import com.example.car.car.Car;
import com.example.car.car.CarService;
import com.example.car.customer.Customer;
import com.example.car.customer.CustomerService;
import com.example.car.customer.DTO.CustomerDTO;
import com.example.car.enums.Enums;
import com.example.car.exception.*;
import com.example.car.rental.DTO.RentalDTO;
import com.example.car.rental.DTO.RentalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public ResponseEntity<CustomResponseEntity> addRental(RentalRequestDTO rentalRequestDTO, UUID customerId){
        UUID carId = rentalRequestDTO.getCarId();
        LocalDateTime start = rentalRequestDTO.getRentalStartDate();
        LocalDateTime end = rentalRequestDTO.getRentalEndDate();


        if (end.isBefore(start) || start.isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Dates are not valid"));
        }

        // Ensure both car and customer exists, if not, throw exception
        Car car = carService.getCarById(carId);
        if (car == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.CAR_NOT_FOUND);

        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.CUSTOMER_NOT_FOUND);


        LocalDateTime licenseStart = customer.getLicenseDate().atStartOfDay();
        long daysPast = Duration.between(licenseStart, LocalDateTime.now()).toDays();
        long requiredDays = 365L * car.getRequiredLicenseYear();
        if (daysPast < requiredDays) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Customer license is not sufficient to rent this car"));
        }

        // Check if car is occupied in this time interval
        List<Enums.Status> invalidStatuses = new ArrayList<>();
        invalidStatuses.add(Enums.Status.ACTIVE);
        invalidStatuses.add(Enums.Status.RESERVED);
        List<Rental> overlaps = rentalRepository.findOverlappingRentals(carId, start, end, invalidStatuses);
        if (!overlaps.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "Car is occupied in this time interval"));
        }

        Rental rental = new Rental();

        rental.setCar(car);
        car.getRentals().add(rental);

        rental.setCustomer(customer);
        customer.getRentals().add(rental);

        rental.setRentalStartDate(start);
        rental.setRentalEndDate(end);
        rental.setStatus(Enums.Status.RESERVED);

        rentalRepository.save(rental);

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Rental has been created successfully"));
    }

    public ResponseEntity<CustomResponseEntity> activateRental(UUID id, LocalDateTime activationTime){
        Rental rental = findRentalById(id);

        if (rental == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.RENTAL_NOT_FOUND);

        // Rental must be in RESERVED status and current time must be after rental's start date
        if (rental.getStatus() != Enums.Status.RESERVED)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Rental status must be RESERVED to be activated"));
        if (rental.getRentalStartDate().isAfter(activationTime))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Activation time must be after rental start date"));


        rental.setActivatedAt(activationTime);
        rental.setStatus(Enums.Status.ACTIVE);
        rentalRepository.save(rental);

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Rental has been activated successfully"));
    }

    public ResponseEntity<CustomResponseEntity> completeRental(UUID id, Integer newKilometer, LocalDateTime completionTime){
        Rental rental = findRentalById(id);

        if (rental == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.RENTAL_NOT_FOUND);

        // Rental status must be ACTIVE
        if (rental.getStatus() != Enums.Status.ACTIVE)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Rental status must be ACTIVE to be completed"));

        if (rental.getActivatedAt().isAfter(completionTime)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Completion time must be after activation time"));
        }

        Car car = rental.getCar();
        if (newKilometer == null || newKilometer < car.getKilometer()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "New kilometer value must be greater than previous kilometer value"));
        }

        LocalDateTime start = rental.getRentalStartDate();
        LocalDateTime end = rental.getRentalEndDate();

        // Calculate base price, we need it in both cases
        long reservedDays = calculateDays(start, end);
        double basePrice = reservedDays * car.getDailyPrice();

        // Rental is completed in reserved interval
        if (end.isAfter(completionTime)){
            rental.setTotalPricePaidByCustomer(basePrice);
        }else{ // Late reservation completion
            long extraDays = calculateDays(start, completionTime) - reservedDays;
            double extraPrice = extraDays * car.getDailyPrice() * 1.5;
            rental.setTotalPricePaidByCustomer(basePrice + extraPrice);
        }

        car.setKilometer(newKilometer);
        carService.saveCar(car);

        rental.setCompletedAt(completionTime);
        rental.setStatus(Enums.Status.COMPLETED);
        rentalRepository.save(rental);

        RentalCompletionResponse response = new RentalCompletionResponse(rental.getTotalPricePaidByCustomer());
        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, response));
    }

    public long calculateDays(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start, end);
        long totalHours = duration.toSeconds();
        long days = totalHours / (24*60*60);
        if (totalHours % (24*60*60) != 0) {
            days++;
        }
        return days;
    }


    public ResponseEntity<CustomResponseEntity> cancelRental(UUID id){
        Rental rental = findRentalById(id);

        if (rental == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.RENTAL_NOT_FOUND);

        // If status is completed or cancelled, we do not cancel them
        if (rental.getStatus() == Enums.Status.COMPLETED)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Rental status must not be COMPLETED"));

        if (rental.getStatus() == Enums.Status.CANCELLED)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Rental already cancelled"));

        rental.setStatus(Enums.Status.CANCELLED);
        rentalRepository.save(rental);

        return ResponseEntity.ok(CustomResponseEntity.OK("Rental has been cancelled successfully"));
    }


    public ResponseEntity<CustomResponseEntity> filterRentals(UUID customerId, UUID carId, Enums.Status status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Rental> rentals = rentalRepository.filterRentals(customerId, carId, status, startDate, endDate);
        List<RentalDTO> rentalsDTO = rentals.stream().map(RentalDTO::new).toList();

        return ResponseEntity.ok(CustomResponseEntity.OK(rentalsDTO));
    }

    public ResponseEntity<CustomResponseEntity> deleteRental(UUID id) {
        Rental rental = findRentalById(id);

        if (rental == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.RENTAL_NOT_FOUND);

        rental.setStatus(Enums.Status.CANCELLED);
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

        return ResponseEntity.ok(CustomResponseEntity.OK("Rental has been deleted successfully"));
    }

    public Rental findRentalById(UUID id){
        Optional<Rental> rental = rentalRepository.findByIdAndNotDeleted(id);

        if (rental.isPresent())
            return rental.get();

        return null;
    }

}
