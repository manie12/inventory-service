package io.inventory_service.datatypes;

public enum AdjustmentStatus {
    DRAFT,
    POSTED,      // applied to on-hand
    REVERSED     // backed out
}