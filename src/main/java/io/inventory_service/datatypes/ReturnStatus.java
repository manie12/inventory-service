package io.inventory_service.datatypes;

public enum ReturnStatus {
    DRAFT,          // created, not yet received
    RECEIVED,       // physically received
    INSPECTED,      // QC done, dispositions decided
    COMPLETED       // all items processed (restocked / scrapped / etc.)
}