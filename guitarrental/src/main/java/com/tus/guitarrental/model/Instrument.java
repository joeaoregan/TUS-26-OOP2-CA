package com.tus.guitarrental.model;

public sealed interface Instrument permits Stringed, Percussion, Amplification {
	String serialNumber();

	String brand();

	String model();

	double baseRentalPrice();
}
