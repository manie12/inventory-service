package io.inventory_service.dtos.receipt;

import java.math.BigDecimal;

public record ReceiptItemDto(
        String sku,
        String variantCode, // e.g., "GREEN-STD" or "WHITE-XL"
        int quantityReceived,
        String purchaseOrderId, // Reference to the PO line item
        BigDecimal unitCost, // Cost of one unit
        String targetLocationId, // Suggested location for put-away
        String batchNo, // Optional: if tracking by batch
        String inspectionResult // "PASS", "FAIL", "PARTIAL_PASS" - will be mapped to enum
) {
}