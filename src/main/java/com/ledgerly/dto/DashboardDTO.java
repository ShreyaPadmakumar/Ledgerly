package com.ledgerly.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private BigDecimal totalSpending;
    private BigDecimal monthlySpending;
    private String topCategory;
    private long totalTransactions;
    private BigDecimal averageExpense;

    // Chart data
    private List<String> trendLabels;
    private List<BigDecimal> trendValues;

    private List<String> categoryLabels;
    private List<BigDecimal> categoryValues;
    private List<String> categoryColors;

    // Recent transactions
    private List<RecentExpense> recentExpenses;

    // Monthly change percentage
    private BigDecimal monthlyChange;
    private boolean monthlyChangePositive;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecentExpense {
        private Long id;
        private BigDecimal amount;
        private String category;
        private String categoryColor;
        private String categoryIcon;
        private String note;
        private String expenseDate;
        private String timeAgo;
    }
}
