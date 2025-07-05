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
        return carRepository.filterCars(make, model, year, id, start, end);
    }

    public Car addCar(CarRequestDTO carRequestDTO) {
        Car car = new Car();
        car.setMake(carRequestDTO.getMake());
        car.setModel(carRequestDTO.getModel());
        car.setYear(carRequestDTO.getYear());
        return carRepository.save(car);
    }

    public void updateCar(UUID id, CarRequestDTO carRequestDTO){
        Car car = getCarById(id);
        car.setMake(carRequestDTO.getMake());
        car.setModel(carRequestDTO.getModel());
        car.setYear(carRequestDTO.getYear());

        carRepository.save(car);
    }

    @Transactional
    public void deleteCar(UUID id){
        Car car = getCarById(id);

        List<Rental> rentals = rentalRepository.filterRentals(null, id, null, null, null);

        for (Rental rental : rentals){
            rental.setStatus(Rental.Status.CANCELLED);
            rental.softDelete();

            Customer customer = rental.getCustomer();
            if (customer != null) {
                customer.getRentals().remove(rental);
                rental.setCustomer(null); // Break reference
            }

            // Break car reference to prevent orphaned links
            rental.setCar(null);
        }

        rentalRepository.saveAll(rentals);
        car.getRentals().clear();
        car.softDelete();
        carRepository.save(car);
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
