package com.example.car.rental;

import com.example.car.car.Car;
import com.example.car.car.CarService;
import com.example.car.customer.Customer;
import com.example.car.customer.CustomerService;
import com.example.car.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    public void addRental(RentalRequestDTO rentalRequestDTO, UUID customerId){
        UUID carId = rentalRequestDTO.getCarId();
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

    public void activateRental(UUID id, LocalDateTime activationTime){
        Rental rental = findRentalById(id);

        // Rental must be in RESERVED status and current time must be after rental's start date
        if (rental.getStatus() != Rental.Status.RESERVED || rental.getRentalStartDate().isAfter(activationTime)){
            throw new RentalCannotBeActivatedException(id);
        }

        rental.setActivatedAt(activationTime);
        rental.setStatus(Rental.Status.ACTIVE);
        rentalRepository.save(rental);
    }

    public double completeRental(UUID id, Integer newKilometer, LocalDateTime completionTime){
        Rental rental = findRentalById(id);

        // Rental status must be ACTIVE
        if (rental.getStatus() != Rental.Status.ACTIVE){
            throw new RentalCannotBeCompletedException(id);
        }

        if (rental.getActivatedAt().isAfter(completionTime)){
            throw new IllegalArgumentException("Completion time must be after activation time");
        }

        Car car = rental.getCar();
        if (newKilometer == null || newKilometer<car.getKilometer()) {
            throw new IllegalArgumentException("new kilometer must be greater than previous kilometer");
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
        rental.setStatus(Rental.Status.COMPLETED);
        rentalRepository.save(rental);

        return rental.getTotalPricePaidByCustomer();
    }

    public long calculateDays(LocalDateTime start, LocalDateTime end){
        Duration duration = Duration.between(start, end);
        long totalHours = duration.toHours();
        long days = totalHours / 24;
        if (totalHours % 24 != 0) {
            days++;
        }
        return days;
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


    public List<RentalDTO> filterRentals(UUID customerId, UUID carId, Rental.Status status, LocalDateTime startDate, LocalDateTime endDate) {
        List<Rental> rentals = rentalRepository.filterRentals(customerId, carId, status, startDate, endDate);
        return rentals.stream()
                .map(RentalDTO::new)
                .toList();
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
