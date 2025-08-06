package io.inventory_service.datatypes;

public enum ReorderPolicy {
    // When to reorder:
    REORDER_POINT,  // Reorder when inventory drops to a certain level
    TIME_BASED,     // Reorder at fixed intervals (e.g., every Monday)
    MIN_MAX,        // Maintain inventory between a minimum and maximum level
    ECONOMIC_ORDER_QUANTITY, // EOQ calculation
    // How much to reorder:
    FIXED_QUANTITY, // Always order a specific quantity
    ORDER_UP_TO_MAX, // Order enough to bring stock up to a max level
    DAYS_OF_SUPPLY // Order enough to cover X days of future demand

}
