package pos.service;

import pos.model.CartItem;
import pos.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CartService {
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal taxRate = new BigDecimal("0.08"); // 8% tax

    public boolean addItem(Product product, int qty) {
        if (product.getStock() < qty) return false;

        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                int newQty = item.getQuantity() + qty;
                if (product.getStock() < newQty) return false;
                item.setQuantity(newQty);
                return true;
            }
        }
        items.add(new CartItem(product, qty));
        return true;
    }

    public boolean removeItem(int index) {
        if (index < 0 || index >= items.size()) return false;
        items.remove(index);
        return true;
    }

    public boolean updateQuantity(int index, int qty) {
        if (index < 0 || index >= items.size()) return false;
        if (qty <= 0) {
            items.remove(index);
            return true;
        }
        CartItem item = items.get(index);
        if (item.getProduct().getStock() < qty) return false;
        item.setQuantity(qty);
        return true;
    }

    public void clear() { items.clear(); }

    public List<CartItem> getItems() { return Collections.unmodifiableList(items); }

    public boolean isEmpty() { return items.isEmpty(); }

    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public BigDecimal getSubtotal() {
        return items.stream()
            .map(CartItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTaxAmount() {
        return getSubtotal().multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getTaxAmount()).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTaxRate() { return taxRate; }
}
