package com.example.car.car;

import com.example.car.CustomResponseEntity;
import com.example.car.car.DTO.AddCarResponse;
import com.example.car.car.DTO.CarDTO;
import com.example.car.car.DTO.CarRequestDTO;
import com.example.car.enums.Enums;
import com.example.car.rental.Rental;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
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
    Validator validator;


    @Autowired
    public CarService(CarRepository carRepository){
        this.carRepository = carRepository;
    }
    // Self-describing

    public ResponseEntity<CustomResponseEntity> filterCars(String make, String model, String color, Integer year, Integer requiredLicenseYear, Integer minPrice, Integer maxPrice, UUID id, String category, Integer gearTypeValue, String licensePlate, Integer kilometer, LocalDateTime start, LocalDateTime end) {
        Enums.GearType gearType = null;
        if (gearTypeValue != null)
            gearType = Enums.GearType.fromValue(gearTypeValue);

        if (start != null && start.isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "Start date must not be in the past"));
        }

        if (start != null && end != null && end.isBefore(start)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseEntity(CustomResponseEntity.BAD_REQUEST, "End date must be after start date"));
        }

        // Exclude ACTIVE and RESERVED cars when filtering
        List<Enums.Status> statuses = List.of(Enums.Status.ACTIVE, Enums.Status.RESERVED);

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

    // If every car is erroneous, throw 400 but if at least 1 of them is correct, create correct ones and give 200 response
    public ResponseEntity<CustomResponseEntity> addCars(List<CarRequestDTO> carRequestDTOList) {
        List<AddCarResponse> responses = new ArrayList<>();

        for (int i = 0; i < carRequestDTOList.size(); i++) {
            CarRequestDTO dto = carRequestDTOList.get(i);
            String carKey = "Car " + (i + 1);

            Set<ConstraintViolation<CarRequestDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder();
                for (ConstraintViolation<CarRequestDTO> violation : violations) {
                    errorMessage.append(violation.getPropertyPath())
                            .append(" ")
                            .append(violation.getMessage())
                            .append("; ");
                }
                AddCarResponse response = new AddCarResponse(null, dto.getLicensePlate(), CustomResponseEntity.BAD_REQUEST(errorMessage));
                responses.add(response);
                continue;
            }

            Car car = new Car(dto);
            carRepository.save(car);

            AddCarResponse response = new AddCarResponse(car.getId(), car.getLicensePlate(), CustomResponseEntity.OK("Successfully created"));
            responses.add(response);
        }

        // Determine the status code based on whether any errors occurred
        boolean allUnsuccessful = responses.stream().allMatch(msg -> msg.getCode().getMessage().startsWith("Invalid"));
        HttpStatus status = allUnsuccessful ? HttpStatus.BAD_REQUEST : HttpStatus.OK;

        return ResponseEntity.status(status)
                .body(new CustomResponseEntity(allUnsuccessful ? CustomResponseEntity.BAD_REQUEST : CustomResponseEntity.OK, responses));
    }


    public ResponseEntity<CustomResponseEntity> updateCar(UUID id, CarRequestDTO carRequestDTO) {
        Car car = getCarById(id);
        if (car == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.CAR_NOT_FOUND);

        String newMake = carRequestDTO.getMake();
        String newModel = carRequestDTO.getModel();
        String newColor = carRequestDTO.getColor();
        Integer newYear = carRequestDTO.getYear();
        Integer newRequiredLicenseYear = carRequestDTO.getRequiredLicenseYear();
        Double newDailyPrice = carRequestDTO.getDailyPrice();
        Integer newKilometer = carRequestDTO.getKilometer();
        String newCategory = carRequestDTO.getCategory();
        Enums.GearType newGearType = Enums.GearType.fromValue(carRequestDTO.getGearType());
        String newLicensePlate = carRequestDTO.getLicensePlate();

        if (newMake != null) {
            if (newMake.equals(car.getMake()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New make cannot be the same as the previous make"));
            car.setMake(newMake);
        }

        if (newModel != null) {
            if (newModel.equals(car.getModel()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New model cannot be the same as the previous model"));
            car.setModel(newModel);
        }

        if (newColor != null) {
            if (newColor.equals(car.getColor()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New color cannot be the same as the previous color"));
            car.setColor(newColor);
        }

        if (newYear != null) {
            if (newYear.equals(car.getYear()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New year cannot be the same as the previous year"));
            car.setYear(newYear);
        }

        if (newRequiredLicenseYear != null) {
            if (newRequiredLicenseYear.equals(car.getRequiredLicenseYear()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New required license year cannot be the same as the previous one"));
            car.setRequiredLicenseYear(newRequiredLicenseYear);
        }

        if (newDailyPrice != null) {
            if (newDailyPrice.equals(car.getDailyPrice()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New daily price cannot be the same as the previous one"));
            car.setDailyPrice(newDailyPrice);
        }

        if (newKilometer != null) {
            if (newKilometer.equals(car.getKilometer()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New kilometer cannot be the same as the previous one"));
            car.setKilometer(newKilometer);
        }

        if (newCategory != null) {
            if (newCategory.equals(car.getCategory()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New category cannot be the same as the previous one"));
            car.setCategory(newCategory);
        }

        if (newGearType != null) {
            if (newGearType.equals(car.getGearType()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New gear type cannot be the same as the previous one"));
            car.setGearType(newGearType);
        }

        if (newLicensePlate != null) {
            if (newLicensePlate.equals(car.getLicensePlate()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new CustomResponseEntity(CustomResponseEntity.CONFLICT, "New license plate cannot be the same as the previous one"));
            car.setLicensePlate(newLicensePlate);
        }

        carRepository.save(car);

        return ResponseEntity.ok(new CustomResponseEntity(CustomResponseEntity.OK, "Car updated successfully"));
    }


    public ResponseEntity<CustomResponseEntity> deleteCar(UUID id) {
        Car car = getCarById(id); // Fetch the non-deleted car

        if (car == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CustomResponseEntity.CAR_NOT_FOUND);
        }

        List<Rental> rentals = car.getRentals(); // Get rentals linked to this car

        // Iterate safely to avoid ConcurrentModificationException
        Iterator<Rental> iterator = rentals.iterator();

        while (iterator.hasNext()) {
            Rental rental = iterator.next();

            // Soft delete rental
            rental.setStatus(Enums.Status.CANCELLED);

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
