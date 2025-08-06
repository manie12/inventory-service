package io.inventory_service.datatypes;

public enum CountVarianceResultStatus {
    MATCH,              // Counted quantity matches system quantity
    OVERAGE,            // Counted quantity is more than system quantity
    SHORTAGE,           // Counted quantity is less than system quantity
    UNACCOUNTED_ITEM    // Item found in physical inventory but not expected by system for this count area
    // Could also add states like 'FOUND_AT_WRONG_LOCATION', 'DAMAGED_DURING_COUNT' etc.
}
