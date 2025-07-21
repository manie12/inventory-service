package io.inventory_service.datatypes;

public enum ReorderTriggerType {
    REORDER_POINT,   // Triggered when stock drops to ROP
    FIXED_INTERVAL,  // Triggered on a schedule (e.g., weekly)
    SALES_FORECAST,  // Triggered by forecast
    MANUAL           // Manually created suggestion

}
