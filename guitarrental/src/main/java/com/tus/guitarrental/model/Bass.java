package com.tus.guitarrental.model;

public record Bass(String serialNumber, String brand, String model, double baseRentalPrice, int numberOfStrings,
		boolean isFretless) implements Instrument {
}
