package com.example.deneme.rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    @Autowired
    RentalService(RentalRepository rentalRepository){
        this.rentalRepository = rentalRepository;
    }

    public void addRental(RentalRequestDTO rentalRequestDTO){
        Rental rental = new Rental();

        rental.setCarId(rentalRequestDTO.getCarId());
        rental.setCustomerId(rentalRequestDTO.getCustomerId());
        rental.setRentalStartDate(rentalRequestDTO.getRentalStartDate());
        rental.setRentalEndDate(rentalRequestDTO.getRentalEndDate());
        rental.setStatus(rentalRequestDTO.getStatus());

        rentalRepository.save(rental);
    }

    public List<Rental> getRentals(){
        return rentalRepository.findAll();
    }
}
