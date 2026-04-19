package com.tus.guitarrental.model;

public record Drums(String serialNumber, String brand, String model, double baseRentalPrice, int numberOfPieces)
		implements Instrument {

}
