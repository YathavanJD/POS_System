package pos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Transaction {
    private static int counter = 1000;

    private String transactionId;
    private List<CartItem> items;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private BigDecimal amountPaid;
    private BigDecimal change;
    private String paymentMethod;
    private LocalDateTime timestamp;
    private String cashierId;

    public Transaction(List<CartItem> items, BigDecimal subtotal, BigDecimal taxAmount,
                       BigDecimal total, BigDecimal amountPaid, BigDecimal change,
                       String paymentMethod, String cashierId) {
        this.transactionId = "TXN-" + (++counter);
        this.items = items;
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.total = total;
        this.amountPaid = amountPaid;
        this.change = change;
        this.paymentMethod = paymentMethod;
        this.timestamp = LocalDateTime.now();
        this.cashierId = cashierId;
    }

    public String getTransactionId() { return transactionId; }
    public List<CartItem> getItems() { return items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public BigDecimal getChange() { return change; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getCashierId() { return cashierId; }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
