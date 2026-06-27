package pos.ui;

import pos.model.*;
import pos.service.*;
import pos.util.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class SalesScreen {
    private final CartService cart;
    private final InventoryService inventory;
    private final SalesService sales;
    private final AuthService auth;

    public SalesScreen(CartService cart, InventoryService inventory,
                       SalesService sales, AuthService auth) {
        this.cart = cart;
        this.inventory = inventory;
        this.sales = sales;
        this.auth = auth;
    }

    public void show() {
        while (true) {
            Console.header("SALES — " + auth.getCurrentUser().getFullName());
            printCart();
            System.out.println();
            System.out.println("  [A] Add item by ID     [S] Search product");
            System.out.println("  [R] Remove item        [U] Update quantity");
            System.out.println("  [C] Checkout           [X] Clear cart");
            System.out.println("  [0] Back to main menu");
            String choice = Console.prompt("\n  Choice: ").toUpperCase();

            switch (choice) {
                case "A" -> addItemById();
                case "S" -> searchAndAdd();
                case "R" -> removeItem();
                case "U" -> updateQuantity();
                case "C" -> checkout();
                case "X" -> clearCart();
                case "0" -> { return; }
                default  -> Console.error("Invalid option.");
            }
        }
    }

    private void printCart() {
        System.out.println("\n  ┌─ CURRENT CART ──────────────────────────────────┐");
        if (cart.isEmpty()) {
            System.out.println("  │  (empty)                                        │");
        } else {
            List<CartItem> items = cart.getItems();
            for (int i = 0; i < items.size(); i++) {
                CartItem item = items.get(i);
                System.out.printf("  │ %2d. %s%n", i + 1, item);
            }
            System.out.println("  ├─────────────────────────────────────────────────┤");
            System.out.printf("  │  Items: %-5d  Subtotal: $%-8.2f Tax: $%-7.2f │%n",
                cart.getItemCount(), cart.getSubtotal(), cart.getTaxAmount());
            System.out.printf("  │  %-20s TOTAL: $%-15.2f │%n", "", cart.getTotal());
        }
        System.out.println("  └─────────────────────────────────────────────────┘");
    }

    private void addItemById() {
        String id = Console.prompt("  Product ID (or barcode): ").toUpperCase();
        Optional<Product> found = id.startsWith("P")
            ? inventory.findById(id)
            : inventory.findByBarcode(id);

        if (found.isEmpty()) {
            Console.error("Product not found.");
            return;
        }
        Product p = found.get();
        System.out.printf("  Found: %s — $%.2f (Stock: %d)%n", p.getName(), p.getPrice(), p.getStock());
        if (p.getStock() == 0) { Console.error("Out of stock."); return; }

        int qty = Console.promptInt("  Quantity: ");
        if (qty <= 0) { Console.error("Quantity must be positive."); return; }

        if (cart.addItem(p, qty)) {
            Console.success("Added " + qty + "x " + p.getName());
        } else {
            Console.error("Not enough stock (available: " + p.getStock() + ")");
        }
    }

    private void searchAndAdd() {
        String query = Console.prompt("  Search: ");
        List<Product> results = inventory.searchByName(query);
        if (results.isEmpty()) { Console.error("No products found."); return; }

        System.out.println();
        for (int i = 0; i < results.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, results.get(i));
        }

        int sel = Console.promptInt("  Select (0=cancel): ");
        if (sel <= 0 || sel > results.size()) return;

        Product p = results.get(sel - 1);
        if (p.getStock() == 0) { Console.error("Out of stock."); return; }
        int qty = Console.promptInt("  Quantity: ");
        if (qty <= 0) { Console.error("Quantity must be positive."); return; }

        if (cart.addItem(p, qty)) {
            Console.success("Added " + qty + "x " + p.getName());
        } else {
            Console.error("Not enough stock (available: " + p.getStock() + ")");
        }
    }

    private void removeItem() {
        if (cart.isEmpty()) { Console.error("Cart is empty."); return; }
        int idx = Console.promptInt("  Item number to remove: ") - 1;
        if (cart.removeItem(idx)) Console.success("Item removed.");
        else Console.error("Invalid item number.");
    }

    private void updateQuantity() {
        if (cart.isEmpty()) { Console.error("Cart is empty."); return; }
        int idx = Console.promptInt("  Item number: ") - 1;
        int qty = Console.promptInt("  New quantity (0 to remove): ");
        if (cart.updateQuantity(idx, qty)) Console.success("Updated.");
        else Console.error("Invalid item or insufficient stock.");
    }

    private void clearCart() {
        if (cart.isEmpty()) { Console.error("Cart is already empty."); return; }
        if (Console.confirm("  Clear all items?")) {
            cart.clear();
            Console.success("Cart cleared.");
        }
    }

    private void checkout() {
        if (cart.isEmpty()) { Console.error("Cart is empty."); return; }

        Console.header("CHECKOUT");
        printCart();

        System.out.println("\n  Payment Method:");
        System.out.println("  [1] Cash  [2] Card  [3] Digital Wallet");
        String pm = Console.prompt("  Choice: ");
        String paymentMethod = switch (pm) {
            case "1" -> "CASH";
            case "2" -> "CARD";
            case "3" -> "DIGITAL WALLET";
            default  -> "CASH";
        };

        BigDecimal total = cart.getTotal();
        BigDecimal amountPaid;
        BigDecimal change;

        if ("CASH".equals(paymentMethod)) {
            while (true) {
                double paid = Console.promptDouble(String.format("  Amount paid ($%.2f due): $", total));
                amountPaid = BigDecimal.valueOf(paid).setScale(2, RoundingMode.HALF_UP);
                if (amountPaid.compareTo(total) < 0) {
                    Console.error(String.format("Insufficient. Need $%.2f more.", total.subtract(amountPaid)));
                } else {
                    change = amountPaid.subtract(total);
                    System.out.printf("  Change: $%.2f%n", change);
                    break;
                }
            }
            change = amountPaid.subtract(total);
        } else {
            amountPaid = total;
            change = BigDecimal.ZERO;
            Console.info("Payment of $" + total + " via " + paymentMethod + " processed.");
        }

        if (!Console.confirm("  Confirm transaction?")) {
            Console.info("Transaction cancelled.");
            return;
        }

        Transaction txn = sales.recordSale(
            cart.getItems(), cart.getSubtotal(), cart.getTaxAmount(),
            total, amountPaid, change, paymentMethod,
            auth.getCurrentUser().getFullName()
        );

        cart.clear();
        ReceiptPrinter.printReceipt(txn);
    }
}
