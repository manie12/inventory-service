package io.inventory_service.datatypes;

public enum ReservationType {
    SALES_ORDER,           // Claim for a customer sales order
    WORK_ORDER,            // Claim for production/assembly needs
    SUBSCRIPTION_BOX,      // Claim for a scheduled subscription delivery
    TRANSFER_ALLOCATION,   // Claim for inventory to be transferred
    QUALITY_HOLD,          // Claim for stock held for QC purposes
    OTHER
}
