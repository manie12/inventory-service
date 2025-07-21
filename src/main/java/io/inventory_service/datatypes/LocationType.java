package io.inventory_service.datatypes;

public enum LocationType {
    STORAGE,             // Standard storage bin
    PICKING,             // Optimized for picking operations
    RECEIVING,           // Area for incoming goods
    SHIPPING,            // Area for outgoing goods
    QUARANTINE,          // Holds items awaiting inspection/disposition
    RETURN,              // Dedicated area for returned goods
    REFURB,              // Area for items undergoing refurbishment
    SALE_FLOOR,          // Retail store sales floor
    BACK_STOCK           // Retail store back stock
}
