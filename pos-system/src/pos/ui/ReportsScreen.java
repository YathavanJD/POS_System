package pos.ui;

import pos.model.CartItem;
import pos.model.Transaction;
import pos.service.SalesService;
import pos.util.Console;
import pos.util.ReceiptPrinter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsScreen {
    private final SalesService sales;

    public ReportsScreen(SalesService sales) {
        this.sales = sales;
    }

    public void show() {
        while (true) {
            Console.header("REPORTS & ANALYTICS");
            System.out.println("  [1] Today's summary");
            System.out.println("  [2] All transactions");
            System.out.println("  [3] View transaction receipt");
            System.out.println("  [4] Top selling items");
            System.out.println("  [0] Back");

            String choice = Console.prompt("\n  Choice: ");
            switch (choice) {
                case "1" -> dailySummary();
                case "2" -> allTransactions();
                case "3" -> viewReceipt();
                case "4" -> topItems();
                case "0" -> { return; }
                default  -> Console.error("Invalid option.");
            }
        }
    }

    private void dailySummary() {
        Console.header("TODAY'S SUMMARY");
        List<Transaction> today = sales.getTransactionsToday();
        System.out.printf("  Transactions : %d%n", today.size());
        System.out.printf("  Total Sales  : $%.2f%n", sales.getTotalSalesToday());

        if (!today.isEmpty()) {
            BigDecimal avgSale = sales.getTotalSalesToday()
                .divide(BigDecimal.valueOf(today.size()), 2, java.math.RoundingMode.HALF_UP);
            System.out.printf("  Average Sale : $%.2f%n", avgSale);

            Map<String, Long> pmCount = today.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentMethod, Collectors.counting()));
            System.out.println("\n  Payment Methods:");
            pmCount.forEach((pm, cnt) -> System.out.printf("    %-20s %d%n", pm, cnt));
        }
        Console.pause();
    }

    private void allTransactions() {
        Console.header("ALL TRANSACTIONS");
        List<Transaction> all = sales.getAllTransactions();
        if (all.isEmpty()) { Console.info("No transactions yet."); Console.pause(); return; }

        System.out.printf("  %-12s %-22s %-8s %-16s %s%n",
            "ID", "Date/Time", "Items", "Payment", "Total");
        System.out.println("  " + "─".repeat(68));

        all.forEach(t -> System.out.printf("  %-12s %-22s %-8d %-16s $%.2f%n",
            t.getTransactionId(), t.getFormattedTimestamp(),
            t.getItems().stream().mapToInt(CartItem::getQuantity).sum(),
            t.getPaymentMethod(), t.getTotal()));

        System.out.println("  " + "─".repeat(68));
        System.out.printf("  %-44s TOTAL: $%.2f%n", "", sales.getTotalSalesAll());
        Console.pause();
    }

    private void viewReceipt() {
        String id = Console.prompt("  Transaction ID: ").toUpperCase();
        sales.findById(id).ifPresentOrElse(
            ReceiptPrinter::printReceipt,
            () -> Console.error("Transaction not found.")
        );
        Console.pause();
    }

    private void topItems() {
        Console.header("TOP SELLING ITEMS");
        Map<String, int[]> totals = new HashMap<>();

        for (Transaction t : sales.getAllTransactions()) {
            for (CartItem item : t.getItems()) {
                String name = item.getProduct().getName();
                totals.computeIfAbsent(name, k -> new int[1])[0] += item.getQuantity();
            }
        }

        if (totals.isEmpty()) { Console.info("No sales data yet."); Console.pause(); return; }

        System.out.printf("  %-30s %s%n", "Product", "Qty Sold");
        System.out.println("  " + "─".repeat(42));

        totals.entrySet().stream()
            .sorted((a, b) -> b.getValue()[0] - a.getValue()[0])
            .limit(10)
            .forEach(e -> System.out.printf("  %-30s %d%n", e.getKey(), e.getValue()[0]));

        Console.pause();
    }
}
