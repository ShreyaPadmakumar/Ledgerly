package com.ledgerly.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting currency values.
 */
public final class CurrencyFormatter {

    private static final NumberFormat INR_FORMAT = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    private CurrencyFormatter() {
        // prevent instantiation
    }

    /**
     * Format a BigDecimal as INR currency string.
     * Example: 1500.50 → ₹1,500.50
     */
    public static String formatINR(BigDecimal amount) {
        if (amount == null) {
            return "₹0.00";
        }
        return INR_FORMAT.format(amount);
    }

    /**
     * Format a BigDecimal with compact notation.
     * Example: 150000 → ₹1.5L
     */
    public static String formatCompact(BigDecimal amount) {
        if (amount == null) return "₹0";

        double value = amount.doubleValue();

        if (value >= 10_000_000) {
            return String.format("₹%.1fCr", value / 10_000_000);
        } else if (value >= 100_000) {
            return String.format("₹%.1fL", value / 100_000);
        } else if (value >= 1_000) {
            return String.format("₹%.1fK", value / 1_000);
        } else {
            return String.format("₹%.0f", value);
        }
    }
}
