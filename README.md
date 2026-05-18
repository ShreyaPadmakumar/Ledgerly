<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" />
  <img src="https://img.shields.io/badge/Tailwind_CSS-3.x-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white" />
  <img src="https://img.shields.io/badge/Chart.js-4.x-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
</p>

# 💰 Ledgerly

**A premium SaaS-style personal finance dashboard** built with Spring Boot, Thymeleaf, and modern frontend tooling. Designed to feel like real fintech software — not a college CRUD project.

---

## ✨ Features

### 📊 Analytics Dashboard
- **KPI Cards** — Total spending, monthly spend, top category, average per transaction
- **Spending Trend Chart** — Monthly line chart with gradient fill (Chart.js)
- **Category Breakdown** — Interactive doughnut chart with color-coded legend
- **Recent Transactions** — Last 5 expenses with relative timestamps
- **Month-over-month change** — Percentage comparison with previous month

### 💳 Expense Management
- Full **CRUD** — Add, edit, delete expenses via animated modals
- **Smart Filtering** — By category, month, and keyword search
- **Pagination** — Navigable pages with filter persistence
- **10 Categories** — Food, Transport, Shopping, Entertainment, Bills, Health, Education, Travel, Groceries, Other

### 🎨 Premium UI/UX
- **Dark Mode** — Toggle with localStorage persistence
- **Responsive** — Mobile, tablet, and desktop layouts
- **Animations** — Fade-in, slide-up, scale transitions
- **Toast Notifications** — Success/error feedback on all actions
- **Empty States** — Friendly prompts when no data exists
- **Glassmorphism** — Frosted navbar with backdrop blur

---

## 🏗️ Architecture

```
Client Browser
      ↓
Thymeleaf Views (HTML + Tailwind + Alpine.js)
      ↓
Spring MVC Controllers
      ↓
Service Layer (Business Logic)
      ↓
Spring Data JPA Repository
      ↓
H2 / MySQL Database
```

---

## 🛠️ Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Backend    | Spring Boot 3.2.5, Spring Data JPA  |
| Frontend   | Thymeleaf, Tailwind CSS, Alpine.js  |
| Charts     | Chart.js 4.x                        |
| Icons      | Lucide Icons                        |
| Database   | H2 (dev) / MySQL (prod)             |
| Validation | Jakarta Bean Validation              |
| Build      | Maven, Java 21                      |

---

## 🚀 Quick Start

### Prerequisites
- Java 21+ (JDK)
- Maven 3.9+ (or use included `mvnw` wrapper)

### Run Locally

```bash
# Clone the repo
git clone https://github.com/ShreyaPadmakumar/ledgerly.git
cd ledgerly

# Run with Maven wrapper
./mvnw spring-boot:run
```

Open **http://localhost:8080** in your browser.

> 30 sample expenses are auto-seeded on first run.

### Switch to MySQL (Optional)

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ledgerly?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

---

## 📁 Project Structure

```
src/main/java/com/ledgerly/
├── config/          # WebConfig, DataSeeder
├── controller/      # DashboardController, ExpenseController
├── dto/             # ExpenseDTO, DashboardDTO
├── entity/          # Expense, Category (enum)
├── repository/      # ExpenseRepository (JPA + custom queries)
├── service/         # ExpenseService, DashboardService
├── util/            # CurrencyFormatter, DateUtils
└── LedgerlyApplication.java

src/main/resources/
├── templates/
│   ├── layouts/     # master.html (base layout)
│   ├── fragments/   # sidebar, navbar, footer
│   ├── dashboard/   # dashboard.html
│   └── expenses/    # expenses.html
└── static/
    ├── css/         # app.css, animations.css, themes.css
    └── js/          # theme.js
```

---

## 📸 Pages

| Page | URL | Description |
|------|-----|-------------|
| Dashboard | `/dashboard` | KPIs, charts, recent transactions |
| Expenses | `/expenses` | Full expense table with CRUD modals |
| H2 Console | `/h2-console` | Database browser (dev only) |

---

## 🗺️ Roadmap

- [x] Phase 1 — MVP (CRUD, Dashboard, Filtering)
- [ ] Phase 2 — Authentication (Spring Security)
- [ ] Phase 2 — CSV/PDF Export
- [ ] Phase 3 — Budget Alerts & Spending Insights
- [ ] Phase 3 — HTMX for live filtering

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

<p align="center">
  Built with ❤️ by <strong>Shreya</strong>
</p>
