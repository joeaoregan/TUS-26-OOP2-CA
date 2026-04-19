package com.tus.guitarrental.model;

public record BassAmplifier(String serialNumber, String brand, String model, double baseRentalPrice, int wattage)
		implements Amplification {

}
