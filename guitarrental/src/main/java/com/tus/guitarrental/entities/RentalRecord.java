package com.tus.guitarrental.entities;

import java.time.LocalDate;

public record RentalRecord(String transactionId, String instrumentName, String customerName, LocalDate rentalDate,
		LocalDate returnDate, double fee) {
}
