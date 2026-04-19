package com.tus.guitarrental.entities;

public record Guitar(String serialNumber, String brand, String model, double baseRentalPrice, int numberOfStrings,
		boolean isElectric) implements Stringed {
}
