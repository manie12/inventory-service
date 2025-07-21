package io.inventory_service.datatypes;

// Represents common reasons for stock adjustments
public enum AdjustmentReasonCode {
    SHRINKAGE,           // Unexplained loss
    DAMAGE,              // Item damaged
    AUDIT_VARIANCE_GAIN, // Physical count higher than system
    AUDIT_VARIANCE_LOSS, // Physical count lower than system
    THEFT,               // Stolen goods
    LOST_IN_TRANSIT,     // Lost during transfer
    DATA_ENTRY_ERROR,    // Mistake during data input
    QUALITY_CONTROL_FAIL,// Failed QC check (e.g., beyond quarantine)
    OTHER,
    CYCLE_COUNT_VARIANCE,
    MISLABEL,
    DATA_CORRECTION,
}