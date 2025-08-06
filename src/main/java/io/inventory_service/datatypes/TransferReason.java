package io.inventory_service.datatypes;

public enum TransferReason {
    STOCK_BALANCING,      // To balance stock levels across locations/warehouses
    CUSTOMER_FULFILLMENT, // To move stock specifically for a customer order (e.g., BOPIS, ship-from-store)
    QUALITY_CONTROL,      // Moving to a QC area
    RETURN_TO_VENDOR,     // Moving out of primary inventory before being sent back to supplier
    OTHER
}
