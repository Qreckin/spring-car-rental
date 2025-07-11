package com.example.car.car;

import com.example.car.CustomResponseEntity;
import com.example.car.car.DTO.CarDTO;
import com.example.car.car.DTO.CarRequestDTO;
import com.example.car.enums.Enums;
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
    public ResponseEntity<CustomResponseEntity> filterCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer licenseYear,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Enums.GearType gearType,
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) Integer kilometer,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return carService.filterCars(make, model, color, year, licenseYear, minPrice, maxPrice, id, category, gearType, licensePlate, kilometer, start, end);
    }


    // If POST request is made to /cars with necessary information in Request Body, create and
    // add the car into the ArrayList. Also provide a http response message
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cars")
    public ResponseEntity<CustomResponseEntity> addCars(@Valid @RequestBody List<CarRequestDTO> carRequestDTOList){
        return carService.addCars(carRequestDTOList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/cars/{id}")
    public ResponseEntity<CustomResponseEntity> updateCar(@PathVariable UUID id, @RequestBody CarRequestDTO updatedCar){
        return carService.updateCar(id, updatedCar);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<CustomResponseEntity> deleteCar(@PathVariable UUID id){
        return carService.deleteCar(id);
    }

}
