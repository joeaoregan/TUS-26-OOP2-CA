package com.tus.guitarrental;

import java.util.Scanner;

import com.tus.guitarrental.controller.StoreController;

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
					selectedAction = GuitarRentalApplication::invalidChoice;
					selectedAction = switch (choice.toLowerCase()) {
					case "0"-> GuitarRentalApplication::formatOutput;
					case "1" -> GuitarRentalApplication::sortAscending;
					case "a" -> GuitarRentalApplication::sortDescending;
					case "2" -> GuitarRentalApplication::testLambda;
					case "3" -> GuitarRentalApplication::testStreams;
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
		System.out.println("\nInventory: "+YELLOW+"Unsorted"+RESET);
		System.out.println("\nSerial Number | Brand     | Model         | Price");
		System.out.println("-----------------------------------------------------");
		controller.getInventory().forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(), i.brand(), i.model(), i.baseRentalPrice()));
	}

	public static void sortAscending() {
		System.out.println("\nInventory: "+YELLOW+"Sorted by Price (Ascending)"+RESET);
//		controller.getInventorySortedByPrice().forEach(System.out::println);
//		controller.getInventorySortedByPrice().forEach(i -> System.out.printf("%s - $%.2f%n", i.serialNumber(), i.baseRentalPrice()));
		System.out.println("\nSerial Number | Brand     | Model         | "+GREEN+"Price ^"+RESET);
		System.out.println("-----------------------------------------------------");
		controller.getInventorySortedByPriceAscending().forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(), i.brand(), i.model(), i.baseRentalPrice()));
	}
	
	public static void sortDescending() {
		System.out.println("\nInventory: "+YELLOW+"Sorted by Price (Descending)"+RESET);
		System.out.println("\nSerial Number | Brand     | Model         | "+GREEN+"Price v"+RESET);
		System.out.println("-----------------------------------------------------");
		controller.getInventorySortedByPriceDescending().forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(), i.brand(), i.model(), i.baseRentalPrice()));
	}
	

	public static void testLambda() {
		System.out.println("\nTesting filtering with lambdas:");
	}

	public static void testStreams() {
		System.out.println("\nTesting streams:");
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
		controller.getInventory().forEach(i -> System.out.printf("%-13s | %-9s | %-13s | €%8.2f%n", i.serialNumber(), i.brand(), i.model(), i.baseRentalPrice()));
	
	}

	public static void printOptions(String choice) {
		String option0, option1, optionA, option2, option3, option4, option5;
		option0 = option1 = optionA = option2 = option3 = option4 = option5 = RESET;
		String highlight = YELLOW;
		if (choice != null) {
			switch (choice) {
			case "0" -> option0 = highlight;
			case "a" -> optionA = highlight;
			case "1" -> option1 = highlight;
			case "2" -> option2 = highlight;
			case "3" -> option3 = highlight;
			case "4" -> option4 = highlight;
			case "5" -> option5 = highlight;
			default -> option1 = option2 = option3 = option4 = option5 = RESET;
			}
		}
		
		System.out.println(GREEN + "\nPlease select an option:" + RESET);
		System.out.println(option0 + "0. View Inventory (Unsorted)");
		System.out.println(option1 + "1. Sorting (Price Ascending)");
		System.out.println(optionA + "a. Sorting (Price Descending)");
		System.out.println(option2 + "2. Filtering (Lambdas & Predicates)");
		System.out.println(option3 + "3. Stream Analytics (min, max, count, collectors)");
		System.out.println(option4 + "4. Fee Calculation (Switch Pattern Matching)");
		System.out.println(option5 + "5. Date/Time API (Due Date Calculation)");
		System.out.println(RESET + "x. Exit");
//		System.out.print("Choice: " + ((choice != null) ? choice : ""));
		System.out.print("Choice: ");
//		option1 = option2 = option3 = option4 = option5 = RESET;
	}

	public static void printLogo() {
		System.out.println(RED + "      ________      .__  __\r\n" + "     /  _____/ __ __|__|/  |______ _______\r\n"
				+ "    /   \\  ___|  |  \\  \\   __\\__  \\\\_  __ \\\r\n"
				+ "    \\    \\_\\  \\  |  /  ||  |  / __ \\|  | \\/\r\n"
				+ "     \\______  /____/|__||__| (____  /__|\r\n" + BLUE + "__________  " + RED + "\\/" + BLUE
				+ "           __       " + RED + "\\/" + BLUE + ".__\r\n" + BLUE
				+ "\\______   \\ ____   _____/  |______  |  |   ______\r\n"
				+ " |       _// __ \\ /    \\   __\\__  \\ |  |  /  ___/\r\n"
				+ " |    |   \\  ___/|   |  \\  |  / __ \\|  |__\\___ \\\r\n"
				+ " |____|_  /\\___  >___|  /__| (____  /____/____  >\r\n"
				+ "        \\/     \\/     \\/          \\/          \\/ ");

		System.out.println(RED + "=================================================");
		System.out.println(GREEN + "       Joe's Six-String Hub: Guitar Rental   " + RED);
		System.out.println("=================================================" + RESET);
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