package com.example.car.rental;

public class RentalCompletionResponse {
    private double totalPricePaidByCustomer;

    // Constructor
    public RentalCompletionResponse(double totalPricePaidByCustomer) {
        this.totalPricePaidByCustomer = totalPricePaidByCustomer;
    }

    // Getter
    public double getTotalPricePaidByCustomer() {
        return totalPricePaidByCustomer;
    }

    // Setter (optional if you want immutability)
    public void setTotalPricePaidByCustomer(double totalPricePaidByCustomer) {
        this.totalPricePaidByCustomer = totalPricePaidByCustomer;
    }
}