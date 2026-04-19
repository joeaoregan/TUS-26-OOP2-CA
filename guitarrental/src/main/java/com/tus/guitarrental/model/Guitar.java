package com.tus.guitarrental.model;

public record Guitar(String serialNumber, String brand, String model, double baseRentalPrice, int numberOfStrings,
		boolean isElectric) implements Instrument {
}
