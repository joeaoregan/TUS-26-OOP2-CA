package com.tus.guitarrental;

public class Logo {// Standard ANSI Color Constants
	public static final String RED = "\u001B[31m";
	public static final String BLUE = "\u001B[34m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String RESET = "\u001B[0m";

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
