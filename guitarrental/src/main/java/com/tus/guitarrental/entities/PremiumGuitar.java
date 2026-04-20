package com.tus.guitarrental.entities;

// Records can implement interfaces, but NOT extend classes
public record PremiumGuitar(String serialNumber, String brand, String model, double baseRentalPrice) implements Instrument {
    
    public PremiumGuitar {
        // Flexible logic inside a Compact Constructor
        if (baseRentalPrice < 2000.00) {
            System.out.println("System Log: Processing premium asset below threshold.");
        }
        
        double insurance = baseRentalPrice * 0.10;
        System.out.printf("Premium Item %s initialised with €%.2f insurance%n", serialNumber, insurance);
    }
}