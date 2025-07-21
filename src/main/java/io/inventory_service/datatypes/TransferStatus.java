package io.inventory_service.datatypes;

public enum TransferStatus {
    INITIATED,    // Transfer requested
    IN_TRANSIT,   // Goods have left source, not yet arrived at destination
    RECEIVED,     // Arrived at destination, awaiting put-away
    COMPLETED,    // Fully put-away at destination
    CANCELLED,
    FAILED,
    REBALANCE,          // move stock to high-demand WH
    STORE_REPLENISH,    // DC → store top-up
    RETURNS_FLOW,       // reverse logistics hub
    DAMAGED_ISOLATION,
    OTHER
}
