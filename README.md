# 👗 Clothify – Clothing Store Inventory Manager

Clothify is a **desktop application** built with **JavaFX** that helps clothing stores manage inventory, track sales, and organize stock efficiently. It provides a **simple, intuitive, and modern interface** to streamline day-to-day operations.

---

## 📌 Table of Contents

- 🌟 Features
- 🖥️ Tech Stack
- 📸 Screenshots
- 🚀 Getting Started
  - 1️⃣ Prerequisites
  - 2️⃣ Installation
  - 3️⃣ Configuration
  - 4️⃣ Running the Application
- 🛠 Usage
- 📚 Documentation
- ❓ FAQ
- 📌 Project Status

---

## 🌟 Features

✅ **Inventory Management** – Easily add, update, and remove clothing items from stock.  
✅ **Sales Tracking** – Record transactions and keep track of revenue.  
✅ **Product Categorization** – Organize products by type, brand, and size.  
✅ **Reporting** – Generate sales and stock reports to analyze business performance.  
✅ **User-Friendly Interface** – Built using **JavaFX** with **SceneBuilder** for a clean UI.  
✅ **Database Versioning** – Managed using **Liquibase** for smooth database updates.  
✅ **Password Reset via Email** – Use **JavaMail API** to reset passwords via email.  
✅ **Default Credentials** – The default admin username is `admin` with password `admin`, and the default employee username is `employee` with password `employee`.

---

## 🖥️ Tech Stack

Clothify is built using the following technologies:

| **Technology**  | **Purpose** |
|----------------|------------|
| **JavaFX** 🎨 | GUI framework for building the desktop application |
| **Hibernate (ORM)** 🛢️ | Object-relational mapping to interact with MySQL database |
| **Liquibase** 📜 | Database change management |
| **MySQL** 🏦 | Relational database for storing application data |
| **SceneBuilder** 🏗️ | UI design tool for FXML-based JavaFX layouts |
| **JavaMail API** 📧 | For sending emails, including password reset functionality |

---

## 📸 Screenshots

<img src="https://github.com/user-attachments/assets/05010703-be47-47d7-95d9-cb184e84fd7e" width="400">

<img src="https://github.com/user-attachments/assets/ada2473a-eed9-4538-8f47-a4f8dcff4780" width="180">

<img src="https://github.com/user-attachments/assets/63e73567-adb1-417f-89f4-71f1230737bf" width="400">

<img src="https://github.com/user-attachments/assets/1b68e86b-cf3f-451a-84c5-e8a5be9418e6" width="400">

<img src="https://github.com/user-attachments/assets/d28e8f7b-7ee1-4b69-b1d0-d1b093db0628" width="180">

<img src="https://github.com/user-attachments/assets/e960783b-e7f9-4313-ac80-57685411677f" width="300">

<img src="https://github.com/user-attachments/assets/b90c6e49-446d-48d0-9a13-25c1cc0d1851" width="300">

<img src="https://github.com/user-attachments/assets/23454499-3389-4e05-8b26-5f4c85dacb22" width="180">

---

## 🚀 Getting Started

### 1️⃣ Prerequisites

Before running Clothify, ensure you have the following installed:

- ✅ **Java 23 (OpenJDK 23.0.2)**
- ✅ **MySQL Server** (for database storage)
- ✅ **SceneBuilder (OPTIONAL)** (for modifying FXML files)

### 2️⃣ Installation

Clone the repository and navigate to the project folder:

```sh
git clone https://github.com/your-username/clothify.git
cd clothify
```

### 3️⃣ Configuration

#### Set up the MySQL Database:

Create a new MySQL database named `clothify_db`.

Edit `hibernate.cfg.xml` and update:

```xml
<property name="hibernate.connection.username">your-username</property>
<property name="hibernate.connection.password">your-password</property>
```

Edit `liquibase.properties` and update:

```makefile
url=jdbc:mysql://localhost:3306/clothify_db
username=your-username
password=your-password
```

#### Apply Database Migrations:

```sh
mvn liquibase:update
```

### 4️⃣ Running the Application

To build and start Clothify, use:

```sh
mvn clean install
java -jar target/clothify.jar
```

---

## 🛠 Usage

1. Open Clothify and log in.
2. Navigate through the **Inventory** section to manage stock.
3. Record sales transactions in the **Sales** section.
4. Generate reports using **JasperReports**.
5. Reset password using **OTP verification via email**.

_(Consider adding screenshots or a demo video here)_

---

## 📚 Documentation

For more details on how to configure, extend, or troubleshoot Clothify, refer to:

📖 **Documentation** find the ER diagram and Use Case diagram from the project

---

## ❓ FAQ

🔹 **Q: Can I use this for multiple stores?**  
**A:** Currently, Clothify is designed for a single-store setup, but multi-store support is planned.

🔹 **Q: Can I use another database instead of MySQL?**  
**A:** Yes! Modify the `hibernate.cfg.xml` and `liquibase.properties` files to configure your database of choice.

---

### 📝 Project Status

📂 Active Development – We’re constantly improving Clothify! 🚀

📢 Follow updates on GitHub.

---
