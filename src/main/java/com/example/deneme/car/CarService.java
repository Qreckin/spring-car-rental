package com.example.deneme.car;

import com.example.deneme.customer.Customer;
import com.example.deneme.customer.CustomerRepository;
import com.example.deneme.exception.CarAlreadyDeletedException;
import com.example.deneme.exception.CarNotFoundException;
import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

// Used @Service annotation to tell Spring that this class is a "bean".
// That is, Spring will create an object from this class and store it in a container
// Then I will use the same object while running the app everywhere this class is used
// with the help of @Autowired
@Service
public class CarService {
    @Autowired
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    private final CustomerRepository customerRepository;


    @Autowired
    public CarService(CarRepository carRepository, RentalRepository rentalRepository, CustomerRepository customerRepository){
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
        this.customerRepository = customerRepository;
    }
    // Self-describing

    public List<Car> filterCars(String make, String model, Integer year, UUID id, LocalDateTime start, LocalDateTime end){
        List<Rental.Status> statuses = List.of(Rental.Status.ACTIVE, Rental.Status.RESERVED);
        return carRepository.filterCars(make, model, year, id, start, end, statuses);
    }

    public Car addCar(CarRequestDTO carRequestDTO) {

        // Adding a car needs no check, we just add it normally
        Car car = new Car();
        car.setMake(carRequestDTO.getMake());
        car.setModel(carRequestDTO.getModel());
        car.setYear(carRequestDTO.getYear());

        return carRepository.save(car); // Returning the Car itself is for passing car ID information and just used in addCar method
    }


    public void updateCar(UUID id, CarRequestDTO carRequestDTO){
        Car car = getCarById(id); // Try to get the car, if car does not exist, throws error

        car.setMake(carRequestDTO.getMake());
        car.setModel(carRequestDTO.getModel());
        car.setYear(carRequestDTO.getYear());

        carRepository.save(car);
    }

    @Transactional // Ensures all DB operations are simultaneous
    public void deleteCar(UUID id) {
        Car car = getCarById(id); // Fetch the non-deleted car

        List<Rental> rentals = car.getRentals(); // Get rentals linked to this car

        // Iterate safely to avoid ConcurrentModificationException
        Iterator<Rental> iterator = rentals.iterator();

        while (iterator.hasNext()) {
            Rental rental = iterator.next();

            // Soft delete rental
            rental.setStatus(Rental.Status.CANCELLED);
            rental.softDelete();

            // Break relationship with Customer
            Customer customer = rental.getCustomer();
            if (customer != null) {
                customer.getRentals().remove(rental); // Remove rental from customer side
                rental.setCustomer(null); // Break back-reference
                customerRepository.save(customer); // Persist customer changes
            }

            // Break relationship with Car
            rental.setCar(null);
            iterator.remove(); // Remove from car.getRentals()

            rentalRepository.save(rental); // Save the soft-deleted rental
        }

        car.softDelete(); // Soft delete the car
        carRepository.save(car); // Save the car (cascading no longer handles removals)
    }


    // Search the car in the ArrayList and return the object if found, null otherwise
    public Car getCarById(UUID id) {
        // Return the car if its found, throw exception otherwise
        return carRepository.findByIdAndNotDeleted(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    public void saveCar(Car car){
        carRepository.save(car);
    }


}
