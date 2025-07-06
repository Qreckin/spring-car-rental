package com.example.deneme.rental.service;

import com.example.deneme.rental.Rental;
import com.example.deneme.rental.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalStatusScheduler {

    @Autowired
    private RentalRepository rentalRepository;

    // Check if there are rentals to activate every 10 seconds
    @Transactional
    @Scheduled(fixedRate = 10000) // every 10 seconds
    public void activateRentals() {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> rentalsToActivate = rentalRepository.findRentalsToActivate(now, Rental.Status.RESERVED);
        for (Rental rental : rentalsToActivate) {
            rental.setStatus(Rental.Status.ACTIVE);
        }
        rentalRepository.saveAll(rentalsToActivate);
    }
}
