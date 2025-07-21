package io.inventory_service.datatypes;

public enum InventoryTransactionType {
    IN,                  // Inbound receipt (e.g., from PO/ASN)
    OUT_SALE,            // Outbound for a sale (shipment)
    OUT_TRANSFER,        // Outbound due to inter-warehouse transfer
    IN_TRANSFER,         // Inbound due to inter-warehouse transfer
    ADJUST_POSITIVE,     // Positive adjustment (e.g., audit gain)
    ADJUST_NEGATIVE,     // Negative adjustment (e.g., shrinkage, damage, audit loss)
    RETURN_CUSTOMER,     // Customer return
    RETURN_SUPPLIER,     // Supplier return
    SCRAP,               // Item scrapped/disposed
    REFURBISHMENT_IN,    // Item received for refurbishment
    REFURBISHMENT_OUT,    // Item sent out for refurbishment
    INBOUND_RECEIPT,            // /stock-receipts
    INTERNAL_TRANSFER_OUT,      // /stock-transfers (source side)
    INTERNAL_TRANSFER_IN,       // /stock-transfers (destination side)
    RESERVATION_PLACED,         // /reservations
    RESERVATION_RELEASED,       // /reservations/{id}/release
    ADJUSTMENT,                 // /stock-adjustments
    RETURN_RESTOCK,             // /returns/restock
    RETURN_DISPOSE,             // scrap / refurbish, etc.
    CYCLE_COUNT_ADJUST,         // variance created by /cycle-counts
    SHIPMENT_CONFIRM            // pick-pack-ship flow (outside scope here)

}