package pos.ui;

import pos.service.AuthService;
import pos.util.Console;

public class UserManagementScreen {
    private final AuthService auth;

    public UserManagementScreen(AuthService auth) {
        this.auth = auth;
    }

    public void show() {
        while (true) {
            Console.header("USER MANAGEMENT");
            System.out.println("  [1] List users");
            System.out.println("  [2] Add user");
            System.out.println("  [3] Remove user");
            System.out.println("  [4] Change password");
            System.out.println("  [0] Back");

            String choice = Console.prompt("\n  Choice: ");
            switch (choice) {
                case "1" -> listUsers();
                case "2" -> addUser();
                case "3" -> removeUser();
                case "4" -> changePassword();
                case "0" -> { return; }
                default  -> Console.error("Invalid option.");
            }
        }
    }

    private void listUsers() {
        Console.header("ALL USERS");
        System.out.printf("  %-6s %-15s %-10s %s%n", "ID", "Username", "Role", "Full Name");
        System.out.println("  " + "─".repeat(48));
        auth.getAllUsers().forEach(u -> System.out.println("  " + u));
        Console.pause();
    }

    private void addUser() {
        Console.header("ADD USER");
        String username = Console.prompt("  Username: ");
        String password = Console.prompt("  Password: ");
        System.out.println("  Role: [1] Admin  [2] Cashier");
        String roleChoice = Console.prompt("  Choice: ");
        String role = "1".equals(roleChoice) ? "ADMIN" : "CASHIER";
        String fullName = Console.prompt("  Full Name: ");

        if (auth.addUser(username, password, role, fullName)) {
            Console.success("User '" + username + "' created as " + role);
        } else {
            Console.error("Username already exists.");
        }
        Console.pause();
    }

    private void removeUser() {
        String username = Console.prompt("  Username to remove: ");
        if (username.equals(auth.getCurrentUser().getUsername())) {
            Console.error("Cannot remove your own account.");
            Console.pause();
            return;
        }
        if (Console.confirm("  Remove user '" + username + "'?")) {
            if (auth.removeUser(username)) Console.success("User removed.");
            else Console.error("User not found or cannot be removed.");
        }
        Console.pause();
    }

    private void changePassword() {
        String username = Console.prompt("  Username: ");
        String newPass  = Console.prompt("  New password: ");
        if (auth.changePassword(username, newPass)) {
            Console.success("Password updated.");
        } else {
            Console.error("User not found.");
        }
        Console.pause();
    }
}
