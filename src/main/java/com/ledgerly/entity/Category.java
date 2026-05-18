package com.ledgerly.entity;

/**
 * Enum representing expense categories.
 * Used for consistent category options across the application.
 */
public enum Category {

    FOOD("Food & Dining", "utensils", "#F59E0B"),
    TRANSPORT("Transport", "car", "#3B82F6"),
    SHOPPING("Shopping", "shopping-bag", "#EC4899"),
    ENTERTAINMENT("Entertainment", "film", "#8B5CF6"),
    BILLS("Bills & Utilities", "zap", "#EF4444"),
    HEALTH("Health", "heart", "#10B981"),
    EDUCATION("Education", "book-open", "#6366F1"),
    TRAVEL("Travel", "plane", "#14B8A6"),
    GROCERIES("Groceries", "shopping-cart", "#F97316"),
    OTHER("Other", "more-horizontal", "#6B7280");

    private final String displayName;
    private final String icon;
    private final String color;

    Category(String displayName, String icon, String color) {
        this.displayName = displayName;
        this.icon = icon;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }
}
