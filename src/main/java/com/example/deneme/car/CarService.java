package com.example.deneme.car;

import com.example.deneme.exception.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Autowired
    public CarService(CarRepository carRepository){
        this.carRepository = carRepository;
    }
    // Self-describing

    public List<Car> filterCars(String make, String model, Integer year, UUID id){
        return carRepository.filterCars(make, model, year, id);
    }
    public List<Car> getCars() {
        return carRepository.findAll();
    }

    public void addCar(CarRequestDTO carRequestDTO) {
        Car car = new Car();
        car.setMake(carRequestDTO.getMake());
        car.setModel(carRequestDTO.getModel());
        car.setYear(carRequestDTO.getYear());
        carRepository.save(car);
    }

    public void updateCar(UUID id, CarRequestDTO carRequestDTO){
        Car car = getCarById(id);
        car.setMake(carRequestDTO.getMake());
        car.setModel(carRequestDTO.getModel());
        car.setYear(carRequestDTO.getYear());

        carRepository.save(car);
    }

    public void removeCar(UUID id){
        Car car = getCarById(id);
        carRepository.delete(car);
    }


    // Search the car in the ArrayList and return the object if found, null otherwise
    public Car getCarById(UUID id) {
        // Return the car if its found, throw exception otherwise
        return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
    }

    public void saveCar(Car car){
        carRepository.save(car);
    }

}
