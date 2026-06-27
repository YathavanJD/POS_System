package pos;

import pos.service.*;
import pos.ui.*;
import pos.util.Console;

public class Main {
    private static final AuthService auth       = new AuthService();
    private static final InventoryService inv   = new InventoryService();
    private static final CartService cart       = new CartService();
    private static final SalesService sales     = new SalesService();

    public static void main(String[] args) {
        printBanner();

        // Login loop
        while (true) {
            Console.header("LOGIN");
            String username = Console.prompt("  Username: ");
            String password = Console.prompt("  Password: ");

            if (auth.login(username, password)) {
                Console.success("Welcome, " + auth.getCurrentUser().getFullName() + "!");
                mainMenu();
            } else {
                Console.error("Invalid credentials. Try again.");
            }
        }
    }

    private static void mainMenu() {
        SalesScreen      salesScreen = new SalesScreen(cart, inv, sales, auth);
        InventoryScreen  invScreen   = new InventoryScreen(inv);
        ReportsScreen    repScreen   = new ReportsScreen(sales);
        UserManagementScreen userScreen = new UserManagementScreen(auth);

        while (auth.isLoggedIn()) {
            Console.header("MAIN MENU — " + auth.getCurrentUser().getRole()
                + " [" + auth.getCurrentUser().getUsername() + "]");

            System.out.println("  [1] New Sale");
            System.out.println("  [2] Inventory" + (auth.isAdmin() ? " (Admin)" : ""));
            System.out.println("  [3] Reports"  + (auth.isAdmin() ? " (Admin)" : ""));
            if (auth.isAdmin()) {
                System.out.println("  [4] User Management");
            }
            System.out.println("  [L] Logout");
            System.out.println("  [Q] Quit");

            String choice = Console.prompt("\n  Choice: ").toUpperCase();
            switch (choice) {
                case "1" -> salesScreen.show();
                case "2" -> {
                    if (auth.isAdmin()) invScreen.show();
                    else Console.error("Admin access required.");
                }
                case "3" -> {
                    if (auth.isAdmin()) repScreen.show();
                    else Console.error("Admin access required.");
                }
                case "4" -> {
                    if (auth.isAdmin()) userScreen.show();
                    else Console.error("Admin access required.");
                }
                case "L" -> {
                    auth.logout();
                    cart.clear();
                    Console.info("Logged out.");
                    return;
                }
                case "Q" -> {
                    System.out.println("\n  Thank you for using Java POS. Goodbye!\n");
                    System.exit(0);
                }
                default -> Console.error("Invalid option.");
            }
        }
    }

    private static void printBanner() {
        System.out.println("""
            
            ╔══════════════════════════════════════════════╗
            ║         JAVA POINT OF SALE SYSTEM            ║
            ║              Version 1.0.0                   ║
            ╚══════════════════════════════════════════════╝
            
              Default accounts:
                admin    / admin123   (Administrator)
                cashier1 / cash1234   (Cashier)
                cashier2 / cash5678   (Cashier)
            """);
    }
}
