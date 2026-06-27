package pos.util;

import pos.model.Transaction;
import pos.model.CartItem;

public class ReceiptPrinter {
    private static final int WIDTH = 44;

    public static void printReceipt(Transaction t) {
        String line = "=".repeat(WIDTH);
        String dash = "-".repeat(WIDTH);

        System.out.println("\n" + line);
        center("JAVA POINT OF SALE");
        center("123 Main Street, City");
        center("Tel: (555) 123-4567");
        System.out.println(dash);
        System.out.printf("%-20s %s%n", "Date:", t.getFormattedTimestamp());
        System.out.printf("%-20s %s%n", "Transaction ID:", t.getTransactionId());
        System.out.printf("%-20s %s%n", "Cashier:", t.getCashierId());
        System.out.println(dash);
        System.out.printf("%-25s %5s %11s%n", "Item", "Qty", "Amount");
        System.out.println(dash);

        for (CartItem item : t.getItems()) {
            String name = item.getProduct().getName();
            if (name.length() > 24) name = name.substring(0, 21) + "...";
            System.out.printf("%-25s %4dx  $%7.2f%n",
                name, item.getQuantity(), item.getSubtotal());
            System.out.printf("  @$%.2f each%n", item.getProduct().getPrice());
        }

        System.out.println(dash);
        System.out.printf("%-30s $%8.2f%n", "Subtotal:", t.getSubtotal());
        System.out.printf("%-30s $%8.2f%n", "Tax (8%):", t.getTaxAmount());
        System.out.println(dash);
        System.out.printf("%-30s $%8.2f%n", "TOTAL:", t.getTotal());
        System.out.println(dash);
        System.out.printf("%-30s $%8.2f%n", "Payment (" + t.getPaymentMethod() + "):", t.getAmountPaid());
        System.out.printf("%-30s $%8.2f%n", "Change:", t.getChange());
        System.out.println(dash);
        center("Thank you for your purchase!");
        center("Please come again!");
        System.out.println(line + "\n");
    }

    private static void center(String text) {
        int padding = (WIDTH - text.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + text);
    }
}
