package pos.util;

import java.util.Scanner;

public class Console {
    private static final Scanner scanner = new Scanner(System.in);

    public static String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    public static int promptInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid number.");
            }
        }
    }

    public static double promptDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid amount.");
            }
        }
    }

    public static boolean confirm(String message) {
        System.out.print(message + " (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    public static void pause() {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
    }

    public static void header(String title) {
        String line = "─".repeat(50);
        System.out.println("\n" + line);
        System.out.println("  " + title);
        System.out.println(line);
    }

    public static void success(String msg) { System.out.println("  ✓ " + msg); }
    public static void error(String msg)   { System.out.println("  ✗ " + msg); }
    public static void info(String msg)    { System.out.println("  ℹ " + msg); }
    public static void warn(String msg)    { System.out.println("  ⚠ " + msg); }
}
