package pos.ui;

import pos.model.Product;
import pos.service.InventoryService;
import pos.util.Console;

import java.math.BigDecimal;
import java.util.List;

public class InventoryScreen {
    private final InventoryService inventory;

    public InventoryScreen(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void show() {
        while (true) {
            Console.header("INVENTORY MANAGEMENT");
            System.out.println("  [1] View all products");
            System.out.println("  [2] Search products");
            System.out.println("  [3] View by category");
            System.out.println("  [4] Add new product");
            System.out.println("  [5] Edit product");
            System.out.println("  [6] Restock product");
            System.out.println("  [7] Remove product");
            System.out.println("  [8] Low stock report");
            System.out.println("  [0] Back");

            String choice = Console.prompt("\n  Choice: ");
            switch (choice) {
                case "1" -> listAll();
                case "2" -> search();
                case "3" -> byCategory();
                case "4" -> addProduct();
                case "5" -> editProduct();
                case "6" -> restock();
                case "7" -> removeProduct();
                case "8" -> lowStockReport();
                case "0" -> { return; }
                default  -> Console.error("Invalid option.");
            }
        }
    }

    private void printHeader() {
        System.out.printf("%n  %-6s %-25s %-12s %-9s %s%n", "ID", "Name", "Category", "Price", "Stock");
        System.out.println("  " + "─".repeat(62));
    }

    private void listAll() {
        Console.header("ALL PRODUCTS");
        printHeader();
        inventory.getAllProducts().forEach(p -> System.out.println("  " + p));
        Console.pause();
    }

    private void search() {
        String q = Console.prompt("  Search term: ");
        List<Product> results = inventory.searchByName(q);
        if (results.isEmpty()) { Console.error("No products found."); Console.pause(); return; }
        printHeader();
        results.forEach(p -> System.out.println("  " + p));
        Console.pause();
    }

    private void byCategory() {
        List<String> cats = inventory.getCategories();
        System.out.println();
        for (int i = 0; i < cats.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, cats.get(i));
        }
        int sel = Console.promptInt("  Select category (0=cancel): ");
        if (sel <= 0 || sel > cats.size()) return;

        String cat = cats.get(sel - 1);
        Console.header("CATEGORY: " + cat);
        printHeader();
        inventory.getByCategory(cat).forEach(p -> System.out.println("  " + p));
        Console.pause();
    }

    private void addProduct() {
        Console.header("ADD NEW PRODUCT");
        String name     = Console.prompt("  Name: ");
        String category = Console.prompt("  Category: ");
        double price    = Console.promptDouble("  Price: $");
        int stock       = Console.promptInt("  Initial stock: ");
        String barcode  = Console.prompt("  Barcode: ");

        Product p = inventory.addProduct(name, category, new BigDecimal(String.valueOf(price)), stock, barcode);
        Console.success("Product added: " + p.getId() + " — " + p.getName());
        Console.pause();
    }

    private void editProduct() {
        String id = Console.prompt("  Product ID to edit: ").toUpperCase();
        inventory.findById(id).ifPresentOrElse(p -> {
            System.out.println("  Current: " + p);
            System.out.println("  (Leave blank to keep current value)");
            String name  = Console.prompt("  New name [" + p.getName() + "]: ");
            String cat   = Console.prompt("  New category [" + p.getCategory() + "]: ");
            String priceStr = Console.prompt("  New price [$" + p.getPrice() + "]: ");

            BigDecimal price = priceStr.isBlank() ? null : new BigDecimal(priceStr);
            if (inventory.updateProduct(id, name.isBlank() ? null : name,
                                        cat.isBlank() ? null : cat, price)) {
                Console.success("Product updated.");
            } else {
                Console.error("Update failed.");
            }
        }, () -> Console.error("Product not found."));
        Console.pause();
    }

    private void restock() {
        String id = Console.prompt("  Product ID to restock: ").toUpperCase();
        inventory.findById(id).ifPresentOrElse(p -> {
            System.out.printf("  %s — Current stock: %d%n", p.getName(), p.getStock());
            int qty = Console.promptInt("  Quantity to add: ");
            inventory.restockProduct(id, qty);
            Console.success("New stock: " + p.getStock());
        }, () -> Console.error("Product not found."));
        Console.pause();
    }

    private void removeProduct() {
        String id = Console.prompt("  Product ID to remove: ").toUpperCase();
        inventory.findById(id).ifPresentOrElse(p -> {
            System.out.println("  Product: " + p);
            if (Console.confirm("  Permanently remove this product?")) {
                if (inventory.removeProduct(id)) Console.success("Removed.");
                else Console.error("Remove failed.");
            }
        }, () -> Console.error("Product not found."));
        Console.pause();
    }

    private void lowStockReport() {
        int threshold = Console.promptInt("  Alert threshold (default 10): ");
        if (threshold <= 0) threshold = 10;
        Console.header("LOW STOCK REPORT (≤ " + threshold + ")");
        List<Product> low = inventory.getLowStockProducts(threshold);
        if (low.isEmpty()) {
            Console.info("No low stock items.");
        } else {
            printHeader();
            low.forEach(p -> System.out.println("  " + p));
        }
        Console.pause();
    }
}
