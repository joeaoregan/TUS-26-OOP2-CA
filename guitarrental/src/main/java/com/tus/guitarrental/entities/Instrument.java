package com.tus.guitarrental.entities;

public sealed interface Instrument permits Stringed, Percussion, Amplification, PremiumGuitar {
	String serialNumber();

	String brand();

	String model();

	double baseRentalPrice();
}
