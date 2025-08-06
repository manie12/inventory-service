package io.inventory_service.datatypes;

public enum CountType {
    ADHOC,       // Initiated manually for a specific purpose (e.g., investigating a discrepancy)
    SCHEDULED,   // Part of a regular, planned cycle counting program
    BLIND,       // Count performed without knowledge of system quantity (good for accuracy)
    VERIFICATION // Re-count of a specific discrepancy
}
