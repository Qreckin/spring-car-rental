package com.example.car.car;

import com.example.car.CustomResponseEntity;
import com.example.car.car.DTO.CarDTO;
import com.example.car.car.DTO.CarRequestDTO;
import com.example.car.customer.Customer;
import com.example.car.customer.CustomerRepository;
import com.example.car.exception.CarNotFoundException;
import com.example.car.rental.Rental;
import com.example.car.rental.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

// Used @Service annotation to tell Spring that this class is a "bean".
// That is, Spring will create an object from this class and store it in a container
// Then I will use the same object while running the app everywhere this class is used
// with the help of @Autowired
@Service
public class CarService {
    private final CarRepository carRepository;


    @Autowired
    public CarService(CarRepository carRepository){
        this.carRepository = carRepository;
    }
    // Self-describing

    public ResponseEntity<CustomResponseEntity> filterCars(String make, String model, String color, Integer year, Integer requiredLicenseYear, Integer minPrice, Integer maxPrice, UUID id, String category, String gearType, String licensePlate, Integer kilometer, LocalDateTime start, LocalDateTime end) {

        if (start != null && start.isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Start date must not be in the past"));
        }

        if (start != null && end != null && end.isBefore(start)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "End date must be after start date"));
        }

        // Exclude ACTIVE and RESERVED cars when filtering
        List<Rental.Status> statuses = List.of(Rental.Status.ACTIVE, Rental.Status.RESERVED);

        // Updated repository call with new parameters
        List<Car> cars = carRepository.filterAvailableCars(make, model, color, year, requiredLicenseYear, minPrice, maxPrice, id, category, gearType, licensePlate, kilometer, start, end, statuses);

        // Convert to DTOs
        List<CarDTO> carDTOs = cars.stream().map(CarDTO::new).toList();

        // Optionally compute total price for selected date range
        if (start != null && end != null) {
            Duration duration = Duration.between(start, end);
            long totalHours = duration.toSeconds();
            long days = totalHours / (24 * 60 * 60);
            if (totalHours % (24*60*60) != 0) {
                days++;
            }

            for (CarDTO car : carDTOs) {
                car.setTotalPrice(car.getDailyPrice() * days);
            }
        }

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, carDTOs));
    }


    // Buraya sorunlu olan carı continue'lama eklenebilir
    public ResponseEntity<CustomResponseEntity> addCars(List<CarRequestDTO> carRequestDTOList) {
        List<Car> carList= new ArrayList<>();
        for (CarRequestDTO request : carRequestDTOList){
            Car car = new Car(request);
            carList.add(car);
            carRepository.save(car);
        }

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, carList));
    }


    public ResponseEntity<CustomResponseEntity> updateCar(UUID id, CarRequestDTO carRequestDTO) {
        Car car = getCarById(id); // Get the car or throw if not found
        if (car == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponseEntity(CustomResponseEntity.NOT_FOUND, "Car with ID: " + id + " is not found"));

        if (carRequestDTO.getMake() != null)
            car.setMake(carRequestDTO.getMake());

        if (carRequestDTO.getModel() != null)
            car.setModel(carRequestDTO.getModel());

        if (carRequestDTO.getColor() != null)
            car.setColor(carRequestDTO.getColor());

        if (carRequestDTO.getYear() != null)
            car.setYear(carRequestDTO.getYear());

        if (carRequestDTO.getRequiredLicenseYear() != null)
            car.setRequiredLicenseYear(carRequestDTO.getRequiredLicenseYear());

        if (carRequestDTO.getDailyPrice() != null)
            car.setDailyPrice(carRequestDTO.getDailyPrice());

        if (carRequestDTO.getKilometer() != null)
            car.setKilometer(carRequestDTO.getKilometer());

        if (carRequestDTO.getCategory() != null)
            car.setCategory(carRequestDTO.getCategory());

        if (carRequestDTO.getGearType() != null)
            car.setGearType(carRequestDTO.getGearType());

        if (carRequestDTO.getLicensePlate() != null)
            car.setLicensePlate(carRequestDTO.getLicensePlate());

        carRepository.save(car);

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Car updated successfully"));
    }

    @Transactional // Ensures all DB operations are simultaneous
    public ResponseEntity<CustomResponseEntity> deleteCar(UUID id) {
        Car car = getCarById(id); // Fetch the non-deleted car

        if (car == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponseEntity(CustomResponseEntity.NOT_FOUND, "Car with ID: " + id + " is not found"));
        }

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
            }

            // Break relationship with Car
            rental.setCar(null);
            iterator.remove(); // Remove from car.getRentals()
        }

        car.softDelete(); // Soft delete the car
        carRepository.save(car); // Save the car (cascading no longer handles removals)

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Car deleted successfully"));
    }


    // Search the car in the ArrayList and return the object if found, null otherwise
    public Car getCarById(UUID id) {
        // Return the car if its found, throw exception otherwise
        Optional<Car> car = carRepository.findByIdAndNotDeleted(id);
        if (car.isPresent())
            return car.get();
        return null;
    }

    public void saveCar(Car car){
        carRepository.save(car);
    }


}
