package com.tus.guitarrental.entities;

public record Bass(String serialNumber, String brand, String model, double baseRentalPrice, int numberOfStrings,
		boolean isFretless) implements Stringed {
	@Override
	public int numberOfStrings() {
		return numberOfStrings;
	}
}
