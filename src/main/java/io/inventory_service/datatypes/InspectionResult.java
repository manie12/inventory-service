package io.inventory_service.datatypes;

public enum InspectionResult {
    GOOD,           // ready for sale
    QUARANTINE,     // needs QA or paperwork
    DAMAGED,
    EXPIRED,
    PASS,
    FAIL,
    PARTIAL_PASS // For cases where some units pass, some fail
}
