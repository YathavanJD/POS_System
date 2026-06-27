# 🛒 Java Point of Sale System

A fully featured, terminal-based **Point of Sale (POS) system** built with pure Java — no frameworks, no external dependencies. Designed for small retail businesses with role-based access, inventory management, sales processing, and reporting.

![Java](https://img.shields.io/badge/Java-23%2B-orange?style=flat-square&logo=openjdk)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=flat-square)

---

## 📸 Preview

```
╔══════════════════════════════════════════════╗
║         JAVA POINT OF SALE SYSTEM            ║
║              Version 1.0.0                   ║
╚══════════════════════════════════════════════╝

──────────────────────────────────────────────────
  MAIN MENU — ADMIN [admin]
──────────────────────────────────────────────────
  [1] New Sale
  [2] Inventory (Admin)
  [3] Reports (Admin)
  [4] User Management
  [L] Logout
  [Q] Quit
```

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 **Authentication** | Secure login with role-based access (Admin / Cashier) |
| 🛍️ **Sales Processing** | Add items by ID, barcode, or name search; process transactions |
| 💳 **Payment Methods** | Cash (with change calculation), Card, and Digital Wallet |
| 🧾 **Receipt Printing** | Formatted receipts printed to terminal after every sale |
| 📦 **Inventory Management** | Add, edit, restock, and remove products; browse by category |
| 📊 **Reports & Analytics** | Daily summaries, transaction history, top-selling items |
| 👥 **User Management** | Admins can create/remove users and reset passwords |
| ⚠️ **Low Stock Alerts** | Configurable threshold alerts for inventory |

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 17 or higher** — [Download from Adoptium](https://adoptium.net)

Verify your Java installation:
```bash
java -version
```

### Installation

```bash
# Clone the repository
git clone https://github.com/your-username/java-pos-system.git

# Navigate into the project
cd java-pos-system
```

### Build & Run

**Windows (Command Prompt):**
```cmd
build.bat
```

**Windows (PowerShell):**
```powershell
# Compile
Get-ChildItem -Recurse -Filter "*.java" src | ForEach-Object { $_.FullName } | Out-File sources.txt
javac -d out (Get-Content sources.txt)

# Run
java -cp out pos.Main
```

**macOS / Linux:**
```bash
chmod +x build.sh
./build.sh
```

**Manual (any platform):**
```bash
# Compile
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# Run
java -cp out pos.Main
```

> After first compile, you only need `java -cp out pos.Main` to start the app.

---

## 🔑 Default Accounts

| Username | Password | Role |
|---|---|---|
| `admin` | `admin123` | Administrator |
| `cashier1` | `cash1234` | Cashier |
| `cashier2` | `cash5678` | Cashier |

> **Tip:** Change default passwords after first login via User Management.

---

## 🗂️ Project Structure

```
java-pos-system/
├── src/
│   └── pos/
│       ├── Main.java                     ← Application entry point
│       ├── model/
│       │   ├── Product.java              ← Product entity
│       │   ├── CartItem.java             ← Cart line item
│       │   ├── Transaction.java          ← Completed sale record
│       │   └── User.java                 ← User/cashier entity
│       ├── service/
│       │   ├── AuthService.java          ← Login & user management logic
│       │   ├── CartService.java          ← Cart operations & tax calculation
│       │   ├── InventoryService.java     ← Product CRUD & stock management
│       │   └── SalesService.java         ← Transaction recording & reporting
│       ├── ui/
│       │   ├── SalesScreen.java          ← Cashier / checkout interface
│       │   ├── InventoryScreen.java      ← Inventory management interface
│       │   ├── ReportsScreen.java        ← Reports & analytics interface
│       │   └── UserManagementScreen.java ← User admin interface
│       └── util/
│           ├── Console.java              ← Input/output helpers
│           └── ReceiptPrinter.java       ← Receipt formatter
├── build.sh                              ← macOS/Linux build script
├── build.bat                             ← Windows build script
└── README.md
```

---

## 🧩 How It Works

### Making a Sale
1. Log in as `cashier1` or `admin`
2. Select **[1] New Sale**
3. Add items using:
   - **[A]** — Enter Product ID (e.g. `P101`)
   - **[S]** — Search by product name
4. Select **[C]** to checkout
5. Choose payment method → enter amount → receipt is printed

### Inventory (Admin only)
- 20 sample products pre-loaded across 7 categories
- Full CRUD: add, edit, restock, and remove products
- Low stock report with configurable threshold

### Reports (Admin only)
- Daily sales totals and transaction count
- Full transaction history with payment breakdown
- Reprint any receipt by Transaction ID
- Top 10 best-selling products

---

## ⚙️ Configuration

| Setting | Location | Default |
|---|---|---|
| Tax Rate | `CartService.java` line 8 | `8%` |
| Low Stock Threshold | Prompt at runtime | `10 units` |
| Receipt Header | `ReceiptPrinter.java` | `JAVA POINT OF SALE` |

---

## 🛣️ Roadmap

- [ ] File-based persistence (save/load data between sessions)
- [ ] SQLite database integration
- [ ] Barcode scanner hardware support
- [ ] Export reports to CSV
- [ ] Discount and coupon system
- [ ] GUI version (JavaFX)

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 👤 Author

**Your Name**
- GitHub: [@your-username](https://github.com/your-username)
- LinkedIn: [your-linkedin](https://linkedin.com/in/your-linkedin)

---

<p align="center">Made with ☕ and Java</p>
