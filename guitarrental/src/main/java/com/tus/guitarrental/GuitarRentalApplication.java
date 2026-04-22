package com.tus.guitarrental;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.tus.guitarrental.controller.StoreController;
import com.tus.guitarrental.entities.Guitar;
import com.tus.guitarrental.entities.Instrument;

import static com.tus.guitarrental.Logo.*;

public class GuitarRentalApplication {

	private final static StoreController controller = new StoreController();

	public static void main(String[] args) {
		boolean exit = false;
		Scanner sc = new Scanner(System.in);
		Runnable selectedAction = null;
		selectedAction = GuitarRentalApplication::formatOutput;
		String choice = null;

		while (!exit) {
			clearConsole();
			printLogo();
			selectedAction.run();
			printOptions(choice);
			selectedAction = GuitarRentalApplication::formatOutput; // Default to showing inventory after an action
			choice = "";

			if (sc.hasNextLine()) {
				choice = sc.nextLine();
				selectedAction = switch (choice.toLowerCase()) {
				case "0" -> GuitarRentalApplication::formatOutput;
				case "1" -> GuitarRentalApplication::sortAscending;
				case "a" -> GuitarRentalApplication::sortDescending;
				case "2" -> GuitarRentalApplication::lambdaPredicate;
				case "b" -> GuitarRentalApplication::lambdaFunction;
				case "c" -> GuitarRentalApplication::lambdaConsumer;
				case "d" -> GuitarRentalApplication::lambdaSupplier;
				case "3" -> GuitarRentalApplication::testStreams;
				case "e" -> GuitarRentalApplication::testStreams2;
				case "4" -> GuitarRentalApplication::testSwitchPattern;
				case "5" -> GuitarRentalApplication::testNioFiles;
				case "6" -> GuitarRentalApplication::testConcurrency;
				case "7" -> GuitarRentalApplication::testLocalisation;
				case "8" -> GuitarRentalApplication::testDateTime;
				case "9" -> GuitarRentalApplication::testFlexibleConstructorBodies;
				case "10" -> GuitarRentalApplication::testJava25Extras;
				case "x" -> {
					System.out.println(RED + "Goodbye!" + RESET);
					exit = true;
					yield null;
				}
				default -> {
					yield GuitarRentalApplication::invalidChoice;
				}
				};
			}
		}
		sc.close();
	}

	public static void formatOutput() {
		System.out.println("\nInventory: " + YELLOW + "Unsorted" + RESET);
		System.out.println("\nSerial Number | Brand     | Model         | Price");
		System.out.println("-----------------------------------------------------");
		controller.getInventory().forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(),
				i.brand(), i.model(), i.baseRentalPrice()));
	}

	public static void sortAscending() {
		System.out.println("\nInventory: " + YELLOW + "Sorted by Price (Ascending)" + RESET);
//		controller.getInventorySortedByPrice().forEach(System.out::println);
//		controller.getInventorySortedByPrice().forEach(i -> System.out.printf("%s - $%.2f%n", i.serialNumber(), i.baseRentalPrice()));
		System.out.println("\nSerial Number | Brand     | Model         | " + GREEN + "Price ^" + RESET);
		System.out.println("-----------------------------------------------------");
		controller.getInventorySortedByPriceAscending()
				.forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(), i.brand(),
						i.model(), i.baseRentalPrice()));
	}

	public static void sortDescending() {
		System.out.println("\nInventory: " + YELLOW + "Sorted by Price (Descending)" + RESET);
		System.out.println("\nSerial Number | Brand     | Model         | " + GREEN + "Price v" + RESET);
		System.out.println("-----------------------------------------------------");
		controller.getInventorySortedByPriceDescending()
				.forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(), i.brand(),
						i.model(), i.baseRentalPrice()));
	}

	// Demonstrates the Predicate<T> interface:
	// Takes an Instrument, returns a boolean to filter the inventory by brand e.g.
	// 'Fender'
	public static void lambdaPredicate() {
		System.out.println("\nInventory: " + YELLOW + "Filter by Brand (Fender)");
		System.out.println("\nSerial Number | " + GREEN + "Brand" + RESET + "     | Model         | Price");
		System.out.println("-----------------------------------------------------");
		controller.filterInventory(i -> i.brand().contains("Fender"))
				.forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(), i.brand(),
						i.model(), i.baseRentalPrice()));
//	    long lines = controller.filterInventory(i -> i.brand().contains("Fender")).size();
		long lines = controller.filterInventoryCount(i -> i.brand().contains("Fender"));
		for (int i = 0; i < controller.getInventorySize() - lines; i++) {
			System.out.println();
		}
	}

	// Demonstrates the Function<T, R> interface:
	// Takes a price (Double), changes it into a new value (Double), applies a 20%
	// VAT calculation
	public static void lambdaFunction() {
//		double priceWithVat = controller.calculatePriceTransformation(item, price -> price * 1.2);
		System.out.println("\nInventory: " + YELLOW + "Apply VAT" + RESET);
		System.out.println("\nSerial Number | Brand     | Model         | Price     | " + GREEN + "VAT (20%)" + RESET);
		System.out.println("-----------------------------------------------------------------");
		controller.getInventory()
				.forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f | %s€%8.2f%n%s", i.serialNumber(),
						i.brand(), i.model(), i.baseRentalPrice(), GREEN,
						controller.calculatePriceTransformation(i, price -> price * 0.2), RESET));
	}

	// Demonstrates the Consumer<T> interface:
	// Performs an operation on each Instrument
	// (logging/processing) without returning a result
	public static void lambdaConsumer() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		String formattedDate = today.format(formatter);

		System.out.println("\nInventory: " + YELLOW + "Processing (Lambdas & Consumers)" + RESET);
		System.out.println("\nInventory Logs:");
		System.out.println("-----------------------------------------------------");
		controller.processInventory(i -> System.out
				.println("LOG: Initialising safety checks for " + i.serialNumber() + " on " + formattedDate));
	}

	// Demonstrates the Supplier<T> interface:
	// Acts as a factory to lazily provide a 'Rare Order' Instrument
	// when a searched item isn't found
	public static void lambdaSupplier() {
		System.out.println("\nInventory: " + YELLOW + "On-Demand Ordering (Lambdas & Suppliers)" + RESET);
		System.out.println("Searching for rare Serial: 'RARE01'...");

		// The Supplier is the "Factory" that only runs if the item is missing
		Instrument result = controller.getOrOrderInstrument("RARE01", () -> {
			System.out.println(RED + "Item not in stock. Generating Special Order..." + RESET);
			return new Guitar("RARE01", "Custom", "Super Rare Guitar", 5000.0, 6, true);
		});

		System.out.println("\nResult of Search/Order:");
		System.out.println("-----------------------------------------------------");
		System.out.printf("%-9s | %-9s | %-13s | €%8.2f%n", result.serialNumber(), result.brand(), result.model(),
				result.baseRentalPrice());

		for (int i = 0; i < 10; i++) {
			System.out.println();
		}
	}

//	public static void lambdaSupplier() {
//		System.out.println("\nInventory: " + YELLOW + "Factory Fallback (Lambdas & Suppliers)" + RESET);
//		System.out.println("\nRetrieval Logs:");
//		System.out.println("-----------------------------------------------------");
//
//		// We pass a Consumer to print the result, and a Supplier to define the fallback
//		controller.processGear(
//				i -> System.out.println("PROCESSING: " + i.brand() + " " + i.model() + " [" + i.serialNumber() + "]"),
//				() -> new Guitar("TEMP-000", "Generic", "Beginner", 10.0, 6, false));
//
//		System.out.println(BLUE
//				+ "\nNote: If inventory is empty, Supplier creates a 'Generic' guitar." + RESET);
//	}
	public static void testStreams() {
		System.out.println(BLUE + "\nInventory: " + YELLOW + "Stream Analytics & Management Reports" + RESET);
		System.out.println("-----------------------------------------------------");

		// Count (Done in your app already, but good to show here)
		System.out.println("Total Assets:\t" + controller.getInventorySize());

		// Min / Max
		controller.getCheapestItem().ifPresent(
				i -> System.out.printf("Budget Option:  %s (%s) - €%.2f%n", i.brand(), i.model(), i.baseRentalPrice()));

		controller.getDearestItem().ifPresent(
				i -> System.out.printf("Premium Option: %s (%s) - €%.2f%n", i.brand(), i.model(), i.baseRentalPrice()));

		// anyMatch
		boolean deals = controller.hasItemsUnder(300.00);
		System.out.println("Are there deals under €300? " + (deals ? "Yes!" : "No."));

		// Collectors.groupingBy
		System.out.println(GREEN + "\nStock Count by Brand:" + RESET);
		controller.getInventoryByBrand().forEach((brand, list) -> // System.out.printf(" - %-13s" + brand + ": " +
																	// list.size() + " items"));
		System.out.printf(" - %-10s: %d items%n", brand, list.size()));

		// Collectors.partitioningBy
		var electricSplit = controller.partitionByElectric();
		System.out.println(GREEN + "\nElectric vs Acoustic Partitioning:" + RESET);
		System.out.println(" - Electric/Active Count:  " + electricSplit.get(true).size());
		System.out.println(" - Acoustic/Passive Count: " + electricSplit.get(false).size());
	}

	public static void testStreams2() {
		System.out.println(BLUE + "\nInventory: " + YELLOW + "Advanced Stream Analytics" + RESET);
		System.out.println("-----------------------------------------------------");

		// findFirst demo
		controller.getFirstByBrand("Fender").ifPresent(
				i -> System.out.println("First Fender found:    " + i.model() + " (" + i.serialNumber() + ")"));

		// findAny demo
		controller.getAnyBudgetOption(500.0)
				.ifPresent(i -> System.out.println("Suggested Budget Item: " + i.brand() + " " + i.model()));

		// allMatch demo
		boolean verified = controller.isInventoryVerified();
		System.out.println("System Integrity Check (All Serials Present):    "
				+ (verified ? GREEN + "PASS" : RED + "FAIL") + RESET);

		// noneMatch demo
		boolean noFreebies = controller.hasNoFreeRentalItems();
		System.out.println("Insurance Check (No €0 rentals):                 "
				+ (noFreebies ? GREEN + "PASS" : RED + "FAIL") + RESET);

		// Collectors.toMap demo
		System.out.println(GREEN + "\nDiscount Report (Serial | 20% Off Price):" + RESET);
		System.out.println("-----------------------------------------------------");
		Map<String, Double> discountMap = controller.getDiscountedPriceMap();
		discountMap.forEach((serial, discountedPrice) -> System.out
				.printf("Serial: %-14s | Summer Sale Price: €%8.2f%n", serial, discountedPrice));
	}

	public static void testSwitchPattern() {
		System.out.println("\nInventory: " + YELLOW + "Rental Fee Calculation" + RESET);
		System.out.println("\nSerial | Type                | Base Price | " + GREEN + "Daily Fee" + RESET);
		System.out.println("-----------------------------------------------------");

		controller.getInventory().forEach(i -> {
			double fee = controller.calculateDailyRentalFee(i);
			String type = i.getClass().getSimpleName();
			System.out.printf("%-6s | %-19s | €%9.2f | €%8.2f%n", i.serialNumber(), type, i.baseRentalPrice(), fee);
		});
	}

	/*
	 * Manage External Assets and Data Files
	 * 
	 * As an administrator, I want to export the current store inventory to an external text file 
	 * using modern I/O practices 
	 * so that I have a persistent record for stocktaking and reporting. 
	 */
	public static void testNioFiles() {
		System.out.println("\nInventory: " + YELLOW + "NIO.2 File Export" + RESET);
		System.out.println("-----------------------------------------------------\n");
		try {
			controller.exportInventoryReport();
			System.out.println(GREEN + "\nSUCCESS:\nCheck project root for 'inventory_report.txt'" + RESET);
		} catch (java.io.IOException e) {
			System.out.println(RED + "ERROR: Failed to write file: " + e.getMessage() + RESET);
		}

		for (int i = 0; i < 9; i++) {
			System.out.println();
		}
	}

	/*
	 * Concurrent Batch Rental with Availability Check
	 * 
	 * As a manager, 
	 * I want to process multiple instrument returns and status checks concurrently 
	 * so that the system remains responsive while handling bulk data updates.
	 */
	public static void testConcurrency() {
		System.out.println("\nInventory: " + YELLOW + "Concurrent Batch Returns" + RESET);
		System.out.println("-----------------------------------------------------");

		// Sample batch of serial numbers to return concurrently
		List<String> returns = List.of("GE001", "BE001", "DA001", "AG001");

		System.out.println("Starting asynchronous inspection for " + returns.size() + " items...");
		controller.processBatchReturns(returns);
		System.out.println(GREEN + "\nAll batch threads have completed processing." + RESET);
	}

	public static void testDateTime() {
		System.out.println("\nInventory: " + YELLOW + "Date/Time API (Rental Due Date)" + RESET);
		System.out.println("-----------------------------------------------------");
		// 1. One week rental example using LocalDate and plusDays
		int duration = 7;
		String due = controller.calculateDueDate(duration);
		System.out.println("\nItem Rented Today: "
				+ LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy")));
		System.out.println("Rental Duration:   " + duration + " days");
		System.out.println(GREEN + "Return Due Date:   " + due + RESET);

		System.out.println("\nInventory: " + YELLOW + "Date/Time API (Enhanced Testing)" + RESET);
		System.out.println("-----------------------------------------------------");

		// 2. Weekly Rental (Demo plusWeeks) - localDate.now(), DateTimeFormatter, ChronoUnit
		String weekly = controller.calculateLongTermDueDate(2, java.time.temporal.ChronoUnit.WEEKS);
		System.out.println("2-Week Touring Rental Due:   " + GREEN + weekly + RESET);

		// 3. Monthly Rental (Demo plusMonths)
		String monthly = controller.calculateLongTermDueDate(1, java.time.temporal.ChronoUnit.MONTHS);
		System.out.println("1-Month Resident Rental Due: " + GREEN + monthly + RESET);

		// 4. Demonstrating Period (Difference between dates)
		LocalDate start = LocalDate.now();
		LocalDate end = start.plusMonths(1).plusWeeks(2);
		java.time.Period period = java.time.Period.between(start, end);
		System.out.printf("Total Rental Duration: %d months and %d days%n", period.getMonths(), period.getDays());

		// 5. ChronoUnit for age calculation (e.g. equipment age in days) - ChronoUnit.DAYS.between()
		LocalDate purchaseDate = LocalDate.of(2024, 1, 1);
		long equipmentAge = controller.getEquipmentAgeInDays(purchaseDate);
		System.out.println("Random Equipment purchased 01/01/2024 Age (Days): " + equipmentAge);

		// 6. Time zones (Demo ZonedDateTime for global system sync)
		System.out.println(GREEN + "\nGlobal System Sync (Time Zones):" + RESET);
		ZonedDateTime dublinTime = ZonedDateTime.now(ZoneId.of("Europe/Dublin"));
		ZonedDateTime nyTime = ZonedDateTime.now(ZoneId.of("America/New_York"));

		// 7. DateTimeFormatter with time zone
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm (z)");
		System.out.println("Dublin HQ Time:   " + dublinTime.format(timeFormatter));
		System.out.println("New York Branch:  " + nyTime.format(timeFormatter));
	}
	
	public static void testLocalisation() {
	    System.out.println("\nInventory: " + YELLOW + "Localisation (Currency Formatting)" + RESET);
	    System.out.println("-----------------------------------------------------\n");
	    
	    double price = 1200.00;
	    System.out.println("Original Price: €" + price);
	    
	    // Use Locale.of for Ireland (Language: en, Country: IE)
	    System.out.println("Ireland (EUR):  " + controller.formatCurrency(price, java.util.Locale.of("en", "IE")));
	    System.out.println("USA (USD):      " + controller.formatCurrency(price, java.util.Locale.US));
	    System.out.println("UK (GBP):       " + controller.formatCurrency(price, java.util.Locale.UK));
	    System.out.println("Germany (EUR):  " + controller.formatCurrency(price, java.util.Locale.GERMANY));
	    System.out.println("Spain (EUR):    " + controller.formatCurrency(price, java.util.Locale.of("es", "ES")));

	    // Localised Date
	    
	    System.out.println("\nInventory: " + YELLOW + "Localisation (Date/Time Formatting)" + RESET);
	    System.out.println("-----------------------------------------------------\n");
	    
	    DateTimeFormatter longFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", java.util.Locale.of("en", "IE"));
	    System.out.println("Localised (Ireland): " + LocalDate.now().format(longFormatter));

	    DateTimeFormatter frFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", java.util.Locale.FRANCE);
	    System.out.println("Localised (France):  " + LocalDate.now().format(frFormatter));

	    DateTimeFormatter grFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", java.util.Locale.GERMANY);
	    System.out.println("Localised (Germany): " + LocalDate.now().format(grFormatter));

	    DateTimeFormatter esFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", java.util.Locale.of("es", "ES"));
	    System.out.println("Localised (Spain):   " + LocalDate.now().format(esFormatter));
	}
	
	public static void testFlexibleConstructorBodies() {
	    System.out.println("\nInventory: " + YELLOW + "Java 25 Flexible Constructor Demo" + RESET);
	    System.out.println("-----------------------------------------------------\n");
	    
	    // This triggers the logic in the PremiumGuitar compact constructor
	    var premium = new com.tus.guitarrental.entities.PremiumGuitar(
	        "PREM1", "PRS", "Custom 24", 4500.00
	    );
	    
	    System.out.println(GREEN + "Premium Record Created: " + premium.brand() + " " + premium.model() + RESET);
	    System.out.println("Base Price: €" + premium.baseRentalPrice());
	    
		for (int i = 0; i < 11; i++) {
			System.out.println();
		}
	}
	 
	public static void testJava25Extras() {
	    System.out.println("\nInventory: " + YELLOW + "Java 25 Extra Marks Demo" + RESET);
	    System.out.println("-----------------------------------------------------");

	    // 1. Demo Stream Gatherers
	    System.out.println(GREEN + "1. Stream Gatherers (Fixed Window Pairs):" + RESET);
	    var bundles = controller.getBundleDeals();
	    bundles.stream().limit(2).forEach(pair -> 
	        System.out.println("Bundle Deal: " + pair.get(0).model() + " + " + pair.get(1).model()));

	    // 2. Demo Scoped Values
	    System.out.println(GREEN + "\n2. Scoped Values (Context-Bound Discount):" + RESET);
	    controller.runDiscountedAudit();
	    
	    // 3. Demo Flexible Constructor
	    System.out.println(GREEN + "\n3. Flexible Constructor (Premium Record):" + RESET);
	    new com.tus.guitarrental.entities.PremiumGuitar("PR-999", "Gibson", "Custom", 5000.00);
	    
//	    System.out.println("\n");
	}

	public static void invalidChoice() {
		System.out.println("\nInventory " + RED + "Invalid choice. Please try again." + RESET);
		System.out.println("\nSerial Number | Brand     | Model         | Price");
		System.out.println("-----------------------------------------------------");
		controller.getInventory().forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(),
				i.brand(), i.model(), i.baseRentalPrice()));

	}

	public static void printOptions(String choice) {
		String option0, option1, optionA, option2, optionB, optionC, optionD, optionE, option3, option4, option5,
				option6, option7, option8, option9, option10;
		option0 = option1 = optionA = option2 = optionB = optionC = optionD = optionE = option3 = option4 = option5 = option6 = option7 = option8 = option9 = option10 = RESET;
		String highlight = YELLOW;
		if (choice != null) {
			switch (choice) {
			case "0" -> option0 = highlight;
			case "1" -> option1 = highlight;
			case "a" -> optionA = highlight;
			case "2" -> option2 = highlight;
			case "b" -> optionB = highlight;
			case "c" -> optionC = highlight;
			case "d" -> optionD = highlight;
			case "3" -> option3 = highlight;
			case "e" -> optionE = highlight;
			case "4" -> option4 = highlight;
			case "5" -> option5 = highlight;
			case "6" -> option6 = highlight;
			case "7" -> option7 = highlight;
			case "8" -> option8 = highlight;
			case "9" -> option9 = highlight;
			case "10" -> option10 = highlight;
			default ->
				option0 = option1 = optionA = option2 = optionB = optionC = optionD = optionE = option3 = option4 = option5 = option6 = option7 = option8 = option9 = option10 = RESET;
			}
		}

		System.out.println(GREEN + "\nPlease select an option:" + RESET);
		System.out.println(option0 + "0. View Inventory (Unsorted)");
		System.out.println(option1 + "1. Sorting (Price Ascending)");
		System.out.println(optionA + "  a. Sorting (Price Descending)");
		System.out.println(option2 + "2. Filtering (Lambdas & Predicates)");
		System.out.println(optionB + "  b. Mapping (Lambdas & Functions)");
		System.out.println(optionC + "  c. Processing (Lambdas & Consumers)");
		System.out.println(optionD + "  d. Factory Fallback (Lambdas & Suppliers)");
		System.out.println(option3 + "3. Stream Analytics (min, max, anyMatch, collectors)");
		System.out.println(optionE + "  e. Stream Management (findFirst, limit)");
		System.out.println(option4 + "4. Fee Calculation (Switch Pattern Matching)");
		System.out.println(option5 + "5. NIO.2 File Export");
		System.out.println(option6 + "6. Concurrency (Batch Processing with Threads)");
		System.out.println(option7 + "7. Localisation (Date/Time Formatting by Locale)");
		System.out.println(option8 + "8. Date/Time API (Due Date Calculation)");
		System.out.println(option9 + "9. Java 25 Record Constructor Demo");
		System.out.println(option10 + "10. Java 25 Extras Demo (Gatherers, Scoped Values, Flexible Constructors)");
		System.out.println(RESET + "x. Exit");
		System.out.print("Choice: ");
	}
}
