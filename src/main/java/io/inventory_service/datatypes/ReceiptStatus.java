package io.inventory_service.datatypes;

public enum ReceiptStatus {
    DRAFT,
    RECEIVED,
    PARTIALLY_RECEIVED,
    CANCELLED,
    PENDING,       // Awaiting arrival
    INSPECTING,    // Undergoing quality/quantity inspection
    COMPLETED,
}
