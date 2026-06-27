package pos.service;

import pos.model.CartItem;
import pos.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SalesService {
    private List<Transaction> transactions = new ArrayList<>();

    public Transaction recordSale(List<CartItem> items, BigDecimal subtotal,
                                   BigDecimal taxAmount, BigDecimal total,
                                   BigDecimal amountPaid, BigDecimal change,
                                   String paymentMethod, String cashierId) {
        // Deduct stock
        for (CartItem item : items) {
            item.getProduct().reduceStock(item.getQuantity());
        }

        Transaction t = new Transaction(
            new ArrayList<>(items), subtotal, taxAmount, total,
            amountPaid, change, paymentMethod, cashierId
        );
        transactions.add(t);
        return t;
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<Transaction> getTransactionsToday() {
        LocalDate today = LocalDate.now();
        return transactions.stream()
            .filter(t -> t.getTimestamp().toLocalDate().equals(today))
            .collect(Collectors.toList());
    }

    public BigDecimal getTotalSalesToday() {
        return getTransactionsToday().stream()
            .map(Transaction::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalSalesAll() {
        return transactions.stream()
            .map(Transaction::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Optional<Transaction> findById(String txnId) {
        return transactions.stream()
            .filter(t -> t.getTransactionId().equals(txnId))
            .findFirst();
    }

    public int getTransactionCountToday() {
        return getTransactionsToday().size();
    }
}
