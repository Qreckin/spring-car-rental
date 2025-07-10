package com.example.car.car;

import com.example.car.car.DTO.CarDTO;
import com.example.car.car.DTO.CarRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// @RestController is used because GET/POST is used
@RestController
public class CarController {
    private final CarService carService;

    // carService is injected by Spring — it uses the CarService bean from the container
    // @Autowired tells Spring to perform dependency injection using this constructor
    @Autowired
    CarController(CarService carService){
        this.carService = carService;
    }

    // If GET request to /cars is made, return an ArrayList containing every car
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/cars")
    public List<CarDTO> filterCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer licenseYear,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String gearType,
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) Integer kilometer,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        if (start != null && start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date must not be in the past");
        }

        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        return carService.filterCars(
                make, model, color, year, licenseYear,
                minPrice, maxPrice, id,
                category, gearType, licensePlate, kilometer,
                start, end
        );
    }


    // If POST request is made to /cars with necessary information in Request Body, create and
    // add the car into the ArrayList. Also provide a http response message
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cars")
    public ResponseEntity<String> addCars(@Valid @RequestBody List<CarRequestDTO> carRequestDTOList){
        List<Car> savedCars = carService.addCars(carRequestDTOList);
        List<UUID> ids = savedCars.stream().map(Car::getId).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body("Car with ID: " + ids + " have been created successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/cars/{id}")
    public ResponseEntity<String> updateCar(@PathVariable UUID id, @RequestBody CarRequestDTO updatedCar){
        carService.updateCar(id, updatedCar);  // UPDATES the car in table, does not create a new CAR in table
        return ResponseEntity.ok("Car with ID: " + id + " has been updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable UUID id){
        carService.deleteCar(id);
        return ResponseEntity.ok("Car with ID: " + id + " has been deleted successfully");
    }

}
