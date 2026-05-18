package com.ledgerly.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date formatting and relative time calculations.
 */
public final class DateUtils {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter MONTH_YEAR_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    private DateUtils() {
        // prevent instantiation
    }

    /**
     * Format date for display: 18 May 2026
     */
    public static String formatDisplay(LocalDate date) {
        if (date == null) return "";
        return date.format(DISPLAY_FORMAT);
    }

    /**
     * Format month and year: May 2026
     */
    public static String formatMonthYear(LocalDate date) {
        if (date == null) return "";
        return date.format(MONTH_YEAR_FORMAT);
    }

    /**
     * Calculate relative time string: "2 days ago", "Just now", etc.
     */
    public static String timeAgo(LocalDate date) {
        if (date == null) return "";

        LocalDate now = LocalDate.now();
        long days = ChronoUnit.DAYS.between(date, now);

        if (days == 0) return "Today";
        if (days == 1) return "Yesterday";
        if (days < 7) return days + " days ago";
        if (days < 30) return (days / 7) + " week" + (days / 7 > 1 ? "s" : "") + " ago";
        if (days < 365) return (days / 30) + " month" + (days / 30 > 1 ? "s" : "") + " ago";
        return (days / 365) + " year" + (days / 365 > 1 ? "s" : "") + " ago";
    }

    /**
     * Get month name from month number (1-12).
     */
    public static String getMonthName(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        if (month < 1 || month > 12) return "";
        return months[month - 1];
    }
}
