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

	public int getInventorySize() {
		return inventory.size();
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

	public List<Instrument> getInventory() {
		return new ArrayList<>(inventory);
	}

	/**
	 * Fundamentals: Sorting - use of Comparator.comparing().
	 * 
	 * User Story: View Sorted Instruments
	 */
	public List<Instrument> getInventorySortedByPriceAscending() {
		return inventory.stream().sorted(Comparator.comparing(Instrument::baseRentalPrice)).toList();
	}

	/**
	 * Fundamentals: Sorting - use of Comparator.comparing().reversed()
	 */
	public List<Instrument> getInventorySortedByPriceDescending() {
		return inventory.stream().sorted(Comparator.comparing(Instrument::baseRentalPrice).reversed()).toList();
	}

	/**
	 * Fundamentals: Lambdas (Predicate) and Streams (filter).
	 * 
	 * User Story: Custom Filter Gear List
	 */
	public List<Instrument> filterInventory(java.util.function.Predicate<Instrument> criteria) {
		return inventory.stream().filter(criteria).toList();
	}

	public long filterInventoryCount(java.util.function.Predicate<Instrument> criteria) {
		return inventory.stream().filter(criteria).count();
	}

	/**
	 * Fundamentals: Lambdas - Function<T, R>
	 * 
	 * Takes a price and applies a transformation (like VAT).
	 */
	public double calculatePriceTransformation(Instrument i, java.util.function.Function<Double, Double> logic) {
		return logic.apply(i.baseRentalPrice());
	}

	public void processInventory(java.util.function.Consumer<Instrument> action) {
		inventory.forEach(action);
	}

	/**
	 * Fundamentals: Lambdas (Consumer, Supplier)
	 */
	public void processGear(java.util.function.Consumer<Instrument> action,
			java.util.function.Supplier<Instrument> defaultGear) {
		if (inventory.isEmpty()) {
			action.accept(defaultGear.get()); // Use Supplier if empty
		} else {
			inventory.forEach(action); // Use Consumer for existing items
		}
	}

	/**
	 * Fundamentals: Supplier If an instrument is not in stock, use the Supplier to
	 * "order" it.
	 */
	public Instrument getOrOrderInstrument(String serial, java.util.function.Supplier<Instrument> orderFactory) {
		return inventory.stream().filter(i -> i.serialNumber().equalsIgnoreCase(serial)).findFirst()
				.orElseGet(orderFactory); // orElseGet takes a Supplier
	}

}