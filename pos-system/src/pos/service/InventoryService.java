package pos.service;

import pos.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryService {
    private Map<String, Product> products = new LinkedHashMap<>();
    private int idCounter = 100;

    public InventoryService() {
        seedProducts();
    }

    private void seedProducts() {
        addProduct("Coca-Cola 330ml",   "Beverages",  new BigDecimal("1.50"), 100, "8901234560001");
        addProduct("Pepsi 330ml",       "Beverages",  new BigDecimal("1.50"), 80,  "8901234560002");
        addProduct("Water 500ml",       "Beverages",  new BigDecimal("0.80"), 200, "8901234560003");
        addProduct("Orange Juice 1L",   "Beverages",  new BigDecimal("3.20"), 50,  "8901234560004");
        addProduct("White Bread",       "Bakery",     new BigDecimal("2.50"), 60,  "8901234560005");
        addProduct("Whole Wheat Bread", "Bakery",     new BigDecimal("3.00"), 40,  "8901234560006");
        addProduct("Croissant",         "Bakery",     new BigDecimal("1.80"), 30,  "8901234560007");
        addProduct("Milk 1L",           "Dairy",      new BigDecimal("2.20"), 70,  "8901234560008");
        addProduct("Cheddar Cheese",    "Dairy",      new BigDecimal("4.50"), 35,  "8901234560009");
        addProduct("Butter 250g",       "Dairy",      new BigDecimal("3.80"), 45,  "8901234560010");
        addProduct("Eggs (12pcs)",      "Dairy",      new BigDecimal("5.00"), 55,  "8901234560011");
        addProduct("Chicken Breast",    "Meat",       new BigDecimal("8.50"), 25,  "8901234560012");
        addProduct("Ground Beef 500g",  "Meat",       new BigDecimal("7.00"), 20,  "8901234560013");
        addProduct("Apples (1kg)",      "Produce",    new BigDecimal("3.50"), 60,  "8901234560014");
        addProduct("Bananas (bunch)",   "Produce",    new BigDecimal("1.20"), 80,  "8901234560015");
        addProduct("Tomatoes (500g)",   "Produce",    new BigDecimal("2.00"), 50,  "8901234560016");
        addProduct("Potato Chips",      "Snacks",     new BigDecimal("2.50"), 90,  "8901234560017");
        addProduct("Chocolate Bar",     "Snacks",     new BigDecimal("1.80"), 120, "8901234560018");
        addProduct("Instant Noodles",   "Dry Goods",  new BigDecimal("0.90"), 150, "8901234560019");
        addProduct("Rice 1kg",          "Dry Goods",  new BigDecimal("2.80"), 80,  "8901234560020");
    }

    public Product addProduct(String name, String category, BigDecimal price, int stock, String barcode) {
        String id = "P" + (++idCounter);
        Product p = new Product(id, name, category, price, stock, barcode);
        products.put(id, p);
        return p;
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id.toUpperCase()));
    }

    public Optional<Product> findByBarcode(String barcode) {
        return products.values().stream()
            .filter(p -> p.getBarcode().equals(barcode))
            .findFirst();
    }

    public List<Product> searchByName(String query) {
        String q = query.toLowerCase();
        return products.values().stream()
            .filter(p -> p.getName().toLowerCase().contains(q))
            .collect(Collectors.toList());
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public List<Product> getByCategory(String category) {
        return products.values().stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .collect(Collectors.toList());
    }

    public List<String> getCategories() {
        return products.values().stream()
            .map(Product::getCategory)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public List<Product> getLowStockProducts(int threshold) {
        return products.values().stream()
            .filter(p -> p.getStock() <= threshold)
            .sorted(Comparator.comparingInt(Product::getStock))
            .collect(Collectors.toList());
    }

    public boolean updateProduct(String id, String name, String category, BigDecimal price) {
        Product p = products.get(id);
        if (p == null) return false;
        if (name != null && !name.isBlank()) p.setName(name);
        if (category != null && !category.isBlank()) p.setCategory(category);
        if (price != null) p.setPrice(price);
        return true;
    }

    public boolean restockProduct(String id, int qty) {
        Product p = products.get(id);
        if (p == null) return false;
        p.addStock(qty);
        return true;
    }

    public boolean removeProduct(String id) {
        return products.remove(id) != null;
    }
}
