package com.tus.guitarrental.controller;

import com.tus.guitarrental.entities.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StoreController {
	private final List<Instrument> inventory = new ArrayList<>();

	public StoreController() {
		populateInventory();
	}

	private void populateInventory() {
		inventory.add(new Guitar("GE001", "Fender", "Stratocaster", 1200.00, 6, true));
		inventory.add(new Guitar("GE002", "Fender", "Telecaster", 1100.00, 6, true));
		inventory.add(new Guitar("GE003", "Fender", "Tom Morello ", 1899.00, 6, true));
		inventory.add(new Guitar("GE004", "Gibson", "Les Paul", 2500.00, 6, true));
		inventory.add(new Guitar("GE005", "Gibson", "SG", 1800.00, 6, true));
		inventory.add(new Bass("BE001", "Washburn", "XB400", 450.00, 4, false));
		inventory.add(new Bass("BE002", "Washburn", "XB105", 389.00, 5, false));
		inventory.add(new Drums("DA001", "Pearl", "Export", 900.00, 5));
		inventory.add(new Drums("DE002", "Alesis", "Debut", 279.00, 5));
		inventory.add(new GuitarAmplifier("AG001", "Marshall", "JVM410H", 1500.00, 100));
		inventory.add(new GuitarAmplifier("AG002", "Peavey", "Bandit", 299.00, 100));
		inventory.add(new BassAmplifier("AB001", "Peavey", "TKO", 479.00, 100));
		inventory.add(new PublicAddressSystem("PA001", "Bose", "L1 Compact", 999.00, 100));
	}

	/**
	 * Fundamentals: Sorting - use of Comparator.comparing() User Story: View Sorted
	 * Instruments
	 */
	public List<Instrument> getInventorySortedByPrice() {
		return inventory.stream().sorted(Comparator.comparing(Instrument::baseRentalPrice)).toList();
	}

	public List<Instrument> getInventory() {
		return new ArrayList<>(inventory);
	}
}