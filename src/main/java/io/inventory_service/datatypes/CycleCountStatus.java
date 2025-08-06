package io.inventory_service.datatypes;

public enum CycleCountStatus {
    PLANNED,
    IN_PROGRESS,
    COMPLETED,      // counting done, variances computed
    POSTED,         // variances posted as stock adjustments
    CANCELLED
}
