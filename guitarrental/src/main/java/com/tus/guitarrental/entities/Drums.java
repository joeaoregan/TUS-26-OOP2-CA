package com.tus.guitarrental.entities;

public record Drums(String serialNumber, String brand, String model, double baseRentalPrice, int numberOfPieces)
		implements Percussion {

}
