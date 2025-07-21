package io.inventory_service.datatypes;

// Represents the current status/condition of an inventory item
public enum InventoryItemCondition {
    A_GRADE,             // Good, sellable condition
    B_GRADE,             // Minor defects, potentially sellable at discount or refurbished
    DAMAGED,             // Significant damage, not sellable as is
    QUARANTINE,          // Awaiting quality inspection
    RETURNED,            // Received as a return, awaiting disposition
    SCRAPPED,            // Marked for disposal
    IN_TRANSIT,          // Currently moving between locations
    UNKNOWN              // Initial or unclassified state
}

