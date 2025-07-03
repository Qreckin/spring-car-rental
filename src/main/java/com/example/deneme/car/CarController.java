package com.example.deneme.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @GetMapping("/cars")
    public List<Car> filterCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year){
        return carService.filterCars(make, model, year);
    }




    // If GET request with a specific ID to /cars is made, return the specific Car object
    @GetMapping("/cars/{id}")
    public Car getCar(@PathVariable UUID id){
        return carService.getCarById(id);
    }

    // If POST request is made to /cars with necessary information in Request Body, create and
    // add the car into the ArrayList. Also provide a http response message
    @PostMapping("/cars")
    public ResponseEntity<String> addCar(@RequestBody CarRequestDTO carRequestDTO){
        carService.addCar(carRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Car has been created successfully");
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<String> updateCar(@PathVariable UUID id, @RequestBody CarRequestDTO updatedCar){
        carService.updateCar(id, updatedCar);  // UPDATES the car in table, does not create a new CAR in table
        return ResponseEntity.ok("Car has been updated successfully.");
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable UUID id){
        carService.removeCar(id);
        return ResponseEntity.ok("Car has been deleted successfully.");
    }

}
