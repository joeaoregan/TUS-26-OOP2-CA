package com.tus.guitarrental;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

import com.tus.guitarrental.controller.StoreController;
import com.tus.guitarrental.entities.Guitar;
import com.tus.guitarrental.entities.Instrument;

public class GuitarRentalApplication {

	private final static StoreController controller = new StoreController();

	private final static String RED = "\u001B[31m";
	private final static String BLUE = "\u001B[34m";
	private final static String GREEN = "\u001B[32m";
	private final static String YELLOW = "\u001B[33m";
	private final static String RESET = "\u001B[0m";

	public static void main(String[] args) {
		boolean exit = false;
		Scanner sc = new Scanner(System.in);
		Runnable selectedAction = null;
		selectedAction = GuitarRentalApplication::formatOutput;
		String choice = null;

		while (!exit) {
			clearConsole();
			printLogo();
//			printOptions(choice);
//			if (selectedAction != null) {
			selectedAction.run();
			printOptions(choice);
//				System.out.println(GREEN + "\nPress ENTER to return to menu..." + RESET);
//				sc.nextLine();
//				selectedAction = null; // Reset after running
			selectedAction = GuitarRentalApplication::formatOutput; // Default to showing inventory after an action
			choice = "";
//			} else {

//				printOptions(choice);
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
				case "5" -> GuitarRentalApplication::testDateTime;
				case "x" -> {
					System.out.println(RED + "Goodbye!" + RESET);
					exit = true;
					yield null;
				}
				default -> {
//		                invalidChoice();
					yield GuitarRentalApplication::invalidChoice;
				}
				};
			}
//			}
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
		discountMap.forEach((serial, discountedPrice) -> // System.out.printf("Serial: %-10s | Special Offer: €%8.2f%n",
															// serial, discountedPrice));
		System.out.printf("Serial: %-14s | Summer Sale Price: €%8.2f%n", serial, discountedPrice));
	}

	public static void testSwitchPattern() {
		System.out.println("\nTesting switch expression with pattern matching:");
	}

	public static void testDateTime() {
		System.out.println("\nTesting date/time API:");
	}

	public static void invalidChoice() {
		System.out.println("\nInventory " + RED + "Invalid choice. Please try again." + RESET);
		System.out.println("\nSerial Number | Brand     | Model         | Price");
		System.out.println("-----------------------------------------------------");
		controller.getInventory().forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(),
				i.brand(), i.model(), i.baseRentalPrice()));

	}

	public static void printOptions(String choice) {
		String option0, option1, optionA, option2, optionB, optionC, optionD, optionE, option3, option4, option5;
		option0 = option1 = optionA = option2 = optionB = optionC = optionD = optionE = option3 = option4 = option5 = RESET;
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
			default ->
				option0 = option1 = optionA = option2 = optionB = optionC = optionD = optionE = option3 = option4 = option5 = RESET;
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
		System.out.println(option5 + "5. Date/Time API (Due Date Calculation)");
		System.out.println(RESET + "x. Exit");
//		System.out.print("Choice: " + ((choice != null) ? choice : ""));
		System.out.print("Choice: ");
//		option1 = option2 = option3 = option4 = option5 = RESET;
	}

	public static void printLogo() {
		System.out
				.println(RED + "        ________      .__  __\r\n" + "       /  _____/ __ __|__|/  |______ _______\r\n"
						+ "      /   \\  ___|  |  \\  \\   __\\__  \\\\_  __ \\\r\n"
						+ "      \\    \\_\\  \\  |  /  ||  |  / __ \\|  | \\/\r\n"
						+ "       \\______  /____/|__||__| (____  /__|\r\n" + BLUE + "  __________  " + RED + "\\/"
						+ BLUE + "           __       " + RED + "\\/" + BLUE + ".__\r\n" + BLUE
						+ "  \\______   \\ ____   _____/  |______  |  |   ______\r\n"
						+ "   |       _// __ \\ /    \\   __\\__  \\ |  |  /  ___/\r\n"
						+ "   |    |   \\  ___/|   |  \\  |  / __ \\|  |__\\___ \\\r\n"
						+ "   |____|_  /\\___  >___|  /__| (____  /____/____  >\r\n"
						+ "          \\/     \\/     \\/          \\/          \\/ ");

		System.out.println(RED + "=====================================================");
		System.out.println(GREEN + "         Joe's Six-String Hub: Guitar Rental   " + RED);
		System.out.println("=====================================================" + RESET);
//		System.out.println("Java Version: " + System.getProperty("java.version"));
	}

	public static void clearConsole() {
		// \033[H moves the cursor to the top-left (Home)
		// \033[2J clears the entire screen
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}
