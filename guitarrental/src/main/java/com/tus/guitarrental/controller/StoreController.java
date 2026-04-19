package com.tus.guitarrental.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.tus.guitarrental.entities.Bass;
import com.tus.guitarrental.entities.BassAmplifier;
import com.tus.guitarrental.entities.Drums;
import com.tus.guitarrental.entities.Guitar;
import com.tus.guitarrental.entities.GuitarAmplifier;
import com.tus.guitarrental.entities.Instrument;
import com.tus.guitarrental.entities.Percussion;
import com.tus.guitarrental.entities.PublicAddressSystem;

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

	// 1. min: Find the cheapest item
	public Optional<Instrument> getCheapestItem() {
		return inventory.stream().min(Comparator.comparing(Instrument::baseRentalPrice));
	}

	// 2. max: Find the most expensive item
	public Optional<Instrument> getDearestItem() {
		return inventory.stream().max(Comparator.comparing(Instrument::baseRentalPrice));
	}

	// 3. anyMatch: Quick check if any items match a condition (e.g., Budget items)
	public boolean hasItemsUnder(double limit) {
		return inventory.stream().anyMatch(i -> i.baseRentalPrice() < limit);
	}

	// 4. groupingBy: Group instruments by their Brand
	public Map<String, List<Instrument>> getInventoryByBrand() {
		return inventory.stream().collect(Collectors.groupingBy(Instrument::brand));
	}

	// 5. partitioningBy: Split inventory into two groups
	// e.g. Electric vs Non-Electric
	public Map<Boolean, List<Instrument>> partitionByElectric() {
		return inventory.stream().collect(Collectors.partitioningBy(i -> i instanceof Guitar g && g.isElectric()));
	}

	// 1. findFirst: Find the first item of a specific brand
	public Optional<Instrument> getFirstByBrand(String brand) {
		return inventory.stream().filter(i -> i.brand().equalsIgnoreCase(brand)).findFirst();
	}

	// 2. findAny: Get any item under a certain budget (efficient for large lists)
	public Optional<Instrument> getAnyBudgetOption(double limit) {
		return inventory.stream().filter(i -> i.baseRentalPrice() < limit).findAny();
	}

	// 3. allMatch: Verification logic (e.g., check if all stock has serial numbers)
	public boolean isInventoryVerified() {
		return inventory.stream().allMatch(i -> i.serialNumber() != null && !i.serialNumber().isBlank());
	}

	// 4. toMap: Create a Price List with a 20% Discount
	public Map<String, Double> getDiscountedPriceMap() {
		return inventory.stream().collect(Collectors.toMap(Instrument::serialNumber, // Key: Serial Number
				i -> i.baseRentalPrice() * 0.8 // Value: Price with 20% discount
		));
	}

	// 5. noneMatch: Check if there are no free rental items (price <= 0)
	public boolean hasNoFreeRentalItems() {
		// Returns true if NO instruments have a base rental price of 0
		return inventory.stream().noneMatch(i -> i.baseRentalPrice() <= 0.0);
	}

	/**
	 * Fundamentals: Switch Expressions & Pattern Matching User Story:
	 * 
	 * Calculate Rental Fee by Instrument Type
	 */
	public double calculateDailyRentalFee(Instrument instrument) {
		// This is the "Switch Expression" - it returns a value directly
		return switch (instrument) {
		// Pattern Matching - check type AND create variable 'g' or 'b'
		case Guitar g when g.isElectric() -> g.baseRentalPrice() * 0.02; // 2% for electrics
		case Guitar g -> g.baseRentalPrice() * 0.015; // 1.5% for acoustics
		case Bass b -> b.baseRentalPrice() * 0.018; // Bass specific rate
		case Percussion p -> p.baseRentalPrice() * 0.012; // Drum specific rate
		default -> instrument.baseRentalPrice() * 0.01;
		};
	}

	/**
	 * Advanced: NIO.2 - Working with Paths and Files
	 * 
	 * User Story: Manage External Assets and Data Files
	 */
	public void exportInventoryReport() throws IOException {
		// Create a path for the report in root of the project
		Path path = Paths.get("inventory_report.txt");

		// Changes inventory into a list of strings for the file
		List<String> lines = inventory.stream()
				.map(i -> String.format("Serial: %s | Brand: %-8s | Model: %-12s | Price: €%.2f", i.serialNumber(),
						i.brand(), i.model(), i.baseRentalPrice()))
				.toList();

		// Write the lines to the file using NIO.2
		Files.write(path, lines, StandardCharsets.UTF_8);
		System.out.println("Report successfully exported to:\n" + path.toAbsolutePath());
	}

	/**
	 * Advanced: Concurrency - ExecutorService User Story: Concurrent Batch Rental
	 * Returns
	 */
	public void processBatchReturns(List<String> serialsToReturn) {
		// Create a thread pool with 3 threads
		ExecutorService executor = Executors.newFixedThreadPool(3);

		for (String serial : serialsToReturn) {
			executor.submit(() -> {
				// Simulate a time-consuming "Return Inspection" process
				System.out.println(Thread.currentThread().getName() + " inspecting item: " + serial);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				System.out.println("FINISHED: Item " + serial + " is now back in stock.");
			});
		}

		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("Batch processing interrupted");
		}
	}

	/**
	 * Fundamentals: Date/Time API
	 * 
	 * User Story: View Rental Return Date
	 */
	public String calculateDueDate(int daysToRent) {
		LocalDate today = LocalDate.now();
		LocalDate dueDate = today.plusDays(daysToRent);
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy");
		return dueDate.format(formatter);
	}

	/**
	 * Fundamentals: Date/Time API - demonstrate Period and arithmetic
	 */
	public String calculateLongTermDueDate(int amount, java.time.temporal.ChronoUnit unit) {
		LocalDate today = LocalDate.now();
		LocalDate dueDate = today.plus(amount, unit);
		return dueDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy"));
	}

	public String calculateBusinessDueDate(int daysToRent) {
		LocalDate dueDate = LocalDate.now().plusDays(daysToRent);

		// If due date is Sunday, adjust to next Monday
		if (dueDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
			dueDate = dueDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		}
		return dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
	}

	public long getEquipmentAgeInDays(LocalDate purchaseDate) {
		return ChronoUnit.DAYS.between(purchaseDate, LocalDate.now());
	}
	
	/**
	 * Advanced: Localisation
	 * 
	 * User Story: Localise User Interface (Currency)
	 */
	public String formatCurrency(double amount, java.util.Locale locale) {
	    java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(locale);
	    return formatter.format(amount);
	}
}
