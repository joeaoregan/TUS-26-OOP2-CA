package com.tus.guitarrental.model;

public sealed interface Instrument permits Guitar, Bass, Drums, Amp {
	String serialNumber();

	String brand();

	String model();

	double baseRentalPrice();
}
