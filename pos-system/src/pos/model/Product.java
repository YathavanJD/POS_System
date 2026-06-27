package pos.model;

import java.math.BigDecimal;

public class Product {
    private String id;
    private String name;
    private String category;
    private BigDecimal price;
    private int stock;
    private String barcode;

    public Product(String id, String name, String category, BigDecimal price, int stock, String barcode) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.barcode = barcode;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public String getBarcode() { return barcode; }

    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }

    public boolean reduceStock(int qty) {
        if (stock < qty) return false;
        stock -= qty;
        return true;
    }

    public void addStock(int qty) { stock += qty; }

    @Override
    public String toString() {
        return String.format("%-6s %-25s %-12s $%-8.2f Stock: %d", id, name, category, price, stock);
    }
}
