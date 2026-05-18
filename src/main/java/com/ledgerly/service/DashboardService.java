package com.ledgerly.service;

import com.ledgerly.dto.DashboardDTO;
import com.ledgerly.entity.Category;
import com.ledgerly.entity.Expense;
import com.ledgerly.repository.ExpenseRepository;
import com.ledgerly.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseRepository expenseRepository;

    /**
     * Build complete dashboard data.
     */
    public DashboardDTO getDashboardData() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // KPI calculations
        BigDecimal totalSpending = expenseRepository.getTotalSpending();
        BigDecimal monthlySpending = expenseRepository.getMonthlySpending(currentMonth, currentYear);
        List<String> topCategories = expenseRepository.getTopCategories(PageRequest.of(0, 1));
        String topCategory = topCategories.isEmpty() ? null : topCategories.get(0);
        long totalTransactions = expenseRepository.getTotalCount();
        BigDecimal averageExpense = expenseRepository.getAverageExpense();

        // Monthly change calculation
        BigDecimal previousMonthSpending;
        if (currentMonth == 1) {
            previousMonthSpending = expenseRepository.getMonthlySpending(12, currentYear - 1);
        } else {
            previousMonthSpending = expenseRepository.getMonthlySpending(currentMonth - 1, currentYear);
        }

        BigDecimal monthlyChange = BigDecimal.ZERO;
        boolean monthlyChangePositive = false;
        if (previousMonthSpending.compareTo(BigDecimal.ZERO) > 0) {
            monthlyChange = monthlySpending.subtract(previousMonthSpending)
                    .divide(previousMonthSpending, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(1, RoundingMode.HALF_UP);
            monthlyChangePositive = monthlyChange.compareTo(BigDecimal.ZERO) > 0;
        }

        // Monthly trend data
        List<Object[]> trendData = expenseRepository.getMonthlyTrend(currentYear);
        List<String> trendLabels = new ArrayList<>();
        List<BigDecimal> trendValues = new ArrayList<>();

        // Initialize all 12 months with zero
        Map<Integer, BigDecimal> trendMap = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            trendMap.put(i, BigDecimal.ZERO);
        }
        for (Object[] row : trendData) {
            int month = ((Number) row[0]).intValue();
            BigDecimal value = (BigDecimal) row[1];
            trendMap.put(month, value);
        }
        for (Map.Entry<Integer, BigDecimal> entry : trendMap.entrySet()) {
            trendLabels.add(DateUtils.getMonthName(entry.getKey()));
            trendValues.add(entry.getValue());
        }

        // Category breakdown
        List<Object[]> categoryData = expenseRepository.getCategoryBreakdown();
        List<String> categoryLabels = new ArrayList<>();
        List<BigDecimal> categoryValues = new ArrayList<>();
        List<String> categoryColors = new ArrayList<>();

        Map<String, String> colorMap = Arrays.stream(Category.values())
                .collect(Collectors.toMap(Category::name, Category::getColor));

        for (Object[] row : categoryData) {
            String cat = (String) row[0];
            BigDecimal value = (BigDecimal) row[1];
            categoryLabels.add(cat);
            categoryValues.add(value);

            // Try to get color from enum, fallback to gray
            String color = colorMap.getOrDefault(cat, "#6B7280");
            // Also try display name match
            if (color.equals("#6B7280")) {
                for (Category c : Category.values()) {
                    if (c.getDisplayName().equals(cat) || c.name().equals(cat)) {
                        color = c.getColor();
                        break;
                    }
                }
            }
            categoryColors.add(color);
        }

        // Recent expenses
        List<Expense> recentList = expenseRepository.findTop5ByOrderByExpenseDateDescCreatedAtDesc();
        List<DashboardDTO.RecentExpense> recentExpenses = recentList.stream()
                .map(e -> {
                    Category cat = findCategory(e.getCategory());
                    return DashboardDTO.RecentExpense.builder()
                            .id(e.getId())
                            .amount(e.getAmount())
                            .category(e.getCategory())
                            .categoryColor(cat != null ? cat.getColor() : "#6B7280")
                            .categoryIcon(cat != null ? cat.getIcon() : "more-horizontal")
                            .note(e.getNote())
                            .expenseDate(DateUtils.formatDisplay(e.getExpenseDate()))
                            .timeAgo(DateUtils.timeAgo(e.getExpenseDate()))
                            .build();
                })
                .collect(Collectors.toList());

        return DashboardDTO.builder()
                .totalSpending(totalSpending)
                .monthlySpending(monthlySpending)
                .topCategory(topCategory != null ? topCategory : "N/A")
                .totalTransactions(totalTransactions)
                .averageExpense(averageExpense.setScale(2, RoundingMode.HALF_UP))
                .monthlyChange(monthlyChange)
                .monthlyChangePositive(monthlyChangePositive)
                .trendLabels(trendLabels)
                .trendValues(trendValues)
                .categoryLabels(categoryLabels)
                .categoryValues(categoryValues)
                .categoryColors(categoryColors)
                .recentExpenses(recentExpenses)
                .build();
    }

    /**
     * Find Category enum by name or display name.
     */
    private Category findCategory(String name) {
        if (name == null) return null;
        for (Category cat : Category.values()) {
            if (cat.name().equalsIgnoreCase(name) || cat.getDisplayName().equalsIgnoreCase(name)) {
                return cat;
            }
        }
        return null;
    }
}
