package com.ledgerly.config;

import com.ledgerly.entity.Category;
import com.ledgerly.entity.Expense;
import com.ledgerly.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * Seeds the database with sample expenses for demonstration.
 * Only runs when the database is empty.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ExpenseRepository expenseRepository;

    @Override
    public void run(String... args) {
        if (expenseRepository.count() > 0) {
            return; // Don't seed if data already exists
        }

        Random random = new Random(42);
        LocalDate today = LocalDate.now();

        List<SeedEntry> entries = List.of(
            new SeedEntry(Category.FOOD, "Lunch at Cafe Coffee Day", 350),
            new SeedEntry(Category.FOOD, "Dinner with friends at Barbeque Nation", 1200),
            new SeedEntry(Category.FOOD, "Swiggy order — biryani", 280),
            new SeedEntry(Category.FOOD, "Groceries from BigBasket", 1850),
            new SeedEntry(Category.TRANSPORT, "Uber to office", 180),
            new SeedEntry(Category.TRANSPORT, "Metro card recharge", 500),
            new SeedEntry(Category.TRANSPORT, "Auto rickshaw", 75),
            new SeedEntry(Category.SHOPPING, "Amazon — headphones", 2499),
            new SeedEntry(Category.SHOPPING, "Myntra — kurta set", 1399),
            new SeedEntry(Category.SHOPPING, "Stationery for notes", 220),
            new SeedEntry(Category.ENTERTAINMENT, "Netflix monthly subscription", 649),
            new SeedEntry(Category.ENTERTAINMENT, "Movie tickets — PVR", 450),
            new SeedEntry(Category.ENTERTAINMENT, "Spotify premium", 119),
            new SeedEntry(Category.BILLS, "Electricity bill", 1450),
            new SeedEntry(Category.BILLS, "WiFi — Jio Fiber", 999),
            new SeedEntry(Category.BILLS, "Mobile recharge — Airtel", 599),
            new SeedEntry(Category.HEALTH, "Gym membership — monthly", 1500),
            new SeedEntry(Category.HEALTH, "Pharmacy — medicines", 380),
            new SeedEntry(Category.EDUCATION, "Udemy course — Spring Boot", 499),
            new SeedEntry(Category.EDUCATION, "Books from Amazon", 850),
            new SeedEntry(Category.TRAVEL, "Train ticket to Mumbai", 1250),
            new SeedEntry(Category.TRAVEL, "Hotel stay — 1 night", 2800),
            new SeedEntry(Category.GROCERIES, "Weekly vegetables", 650),
            new SeedEntry(Category.GROCERIES, "Fruits and dairy", 420),
            new SeedEntry(Category.OTHER, "Haircut at salon", 300),
            new SeedEntry(Category.OTHER, "Donation — temple", 201),
            new SeedEntry(Category.FOOD, "Dominos pizza night", 550),
            new SeedEntry(Category.TRANSPORT, "Ola ride to mall", 220),
            new SeedEntry(Category.BILLS, "Water bill", 350),
            new SeedEntry(Category.SHOPPING, "Flipkart — phone case", 399)
        );

        for (int i = 0; i < entries.size(); i++) {
            SeedEntry entry = entries.get(i);
            // Spread expenses across last 4 months
            int daysBack = random.nextInt(120);
            LocalDate date = today.minusDays(daysBack);

            Expense expense = Expense.builder()
                    .amount(BigDecimal.valueOf(entry.amount))
                    .category(entry.category.name())
                    .note(entry.note)
                    .expenseDate(date)
                    .build();

            expenseRepository.save(expense);
        }

        System.out.println("✅ Seeded " + entries.size() + " sample expenses.");
    }

    private record SeedEntry(Category category, String note, double amount) {}
}
