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
		String choice = null;

		while (!exit) {
			clearConsole();
			printLogo();
			printOptions(choice);
			if (selectedAction != null) {
				selectedAction.run();
				System.out.println(GREEN + "\nPress ENTER to return to menu..." + RESET);
				sc.nextLine();
				selectedAction = null; // Reset after running
				choice = "";
			} else {

				if (sc.hasNextLine()) {
					choice = sc.nextLine();
					selectedAction = GuitarRentalApplication::invalidChoice;
					selectedAction = switch (choice) {
					case "1" -> GuitarRentalApplication::testSort;
					case "2" -> GuitarRentalApplication::testLambda;
					case "3" -> GuitarRentalApplication::testStreams;
					case "4" -> GuitarRentalApplication::testSwitchPattern;
					case "5" -> GuitarRentalApplication::testDateTime;
					case "0" -> {
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
			}
		}
		sc.close();
	}

//	public static void main(String[] args) {
//		boolean exit = false;
//		Scanner sc = new Scanner(System.in);
//
//		while (!exit) {
//			clearConsole();
//			printLogo();
//			printOptions();
//
//			if (sc.hasNextLine()) {
//				String choice = sc.nextLine();
//
//				switch (choice) {
//				case "1" -> testSort();
//				case "2" -> testLambda();
//				case "3" -> testStreams();
//				case "4" -> testSwitchPattern();
//				case "5" -> testDateTime();
//				case "0" -> {
//					System.out.println(RED + "Thank you for visiting Joe's Six-String Hub! Goodbye!" + RESET);
//					exit = true;
//				}
//				default -> System.out.println(RED + "Invalid choice. Please try again." + RESET);
//				}
//			}
//		}
//
//		sc.close();
//	}

	public static void testSort() {
		System.out.println("\nTesting sorting by price:");
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
		System.out.println(RED + "\nInvalid choice. Please try again." + RESET);
	}

	public static void printOptions(String choice) {
		String option1, option2, option3, option4, option5;
		option1 = option2 = option3 = option4 = option5 = RESET;
		String highlight = YELLOW;
		if (choice != null) {
			switch (choice) {
			case "1" -> option1 = highlight;
			case "2" -> option2 = highlight;
			case "3" -> option3 = highlight;
			case "4" -> option4 = highlight;
			case "5" -> option5 = highlight;
			default -> option1 = option2 = option3 = option4 = option5 = RESET;
			}
			;
		}
		
		System.out.println(GREEN + "\nPlease select an option:" + RESET);
		System.out.println(option1 + "1. Sorting (Comparator.comparing)");
		System.out.println(option2 + "2. Filtering (Lambdas & Predicates)");
		System.out.println(option3 + "3. Stream Analytics (min, max, count, collectors)");
		System.out.println(option4 + "4. Fee Calculation (Switch Pattern Matching)");
		System.out.println(option5 + "5. Date/Time API (Due Date Calculation)");
		System.out.println(RESET + "0. Exit");
		System.out.print("Choice: " + ((choice != null) ? choice : ""));
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
