# Java Point of Sale System

A full-featured terminal-based POS system written in pure Java — no external libraries required.

---

## Features

- **Sales / Cashier Screen** — Add items by product ID, barcode, or search; process payments (cash, card, digital wallet); print receipts; give change
- **Inventory Management** — View, search, add, edit, restock, and remove products; browse by category; low stock alerts
- **Reports** — Daily sales summary, all transactions, reprint receipts, top-selling items
- **User Management** — Admin can add/remove users and change passwords
- **Role-based access** — Admins get full access; Cashiers can only make sales

---

## Requirements

- **Java 17 or higher** (JDK, not just JRE)

To check your Java version:
```
java -version
```

Download Java: https://adoptium.net  (free, open-source)

---

## How to Build & Run

### Option 1 — Use the build script (Recommended)

**Mac / Linux:**
```bash
chmod +x build.sh
./build.sh
```

**Windows:**
```
build.bat
```

This compiles all source files, creates `JavaPOS.jar`, and offers to run it.

---

### Option 2 — Manual steps

**Step 1: Compile**
```bash
# Mac/Linux
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Windows
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
```

**Step 2: Package**
```bash
echo Main-Class: pos.Main > manifest.txt
jar cfm JavaPOS.jar manifest.txt -C out .
```

**Step 3: Run**
```bash
java -jar JavaPOS.jar
```

---

## Default Login Accounts

| Username  | Password  | Role         |
|-----------|-----------|--------------|
| admin     | admin123  | Administrator|
| cashier1  | cash1234  | Cashier      |
| cashier2  | cash5678  | Cashier      |

---

## Project Structure

```
pos-system/
├── src/
│   └── pos/
│       ├── Main.java                  ← Entry point
│       ├── model/
│       │   ├── Product.java
│       │   ├── CartItem.java
│       │   ├── Transaction.java
│       │   └── User.java
│       ├── service/
│       │   ├── AuthService.java
│       │   ├── CartService.java
│       │   ├── InventoryService.java
│       │   └── SalesService.java
│       ├── ui/
│       │   ├── SalesScreen.java
│       │   ├── InventoryScreen.java
│       │   ├── ReportsScreen.java
│       │   └── UserManagementScreen.java
│       └── util/
│           ├── Console.java
│           └── ReceiptPrinter.java
├── build.sh       ← Mac/Linux build script
├── build.bat      ← Windows build script
└── README.md
```

---

## Usage Guide

### Making a Sale
1. Log in as `cashier1` or `admin`
2. Select **[1] New Sale**
3. Press **[A]** to add items by Product ID (e.g. `P101`)
4. Press **[S]** to search products by name
5. Press **[C]** to checkout — choose payment method and enter amount

### Inventory (Admin only)
- View all 20 pre-loaded products across 7 categories
- Add new products, edit prices, restock, or remove items

### Reports (Admin only)
- Check daily totals, view all transactions, reprint any receipt

---

## Notes

- All data is **in-memory** — it resets when you quit. This is by design for a demo system; persistence can be added with a database or file I/O.
- The tax rate is 8% (configurable in `CartService.java`)
- 20 sample products are pre-loaded on startup
