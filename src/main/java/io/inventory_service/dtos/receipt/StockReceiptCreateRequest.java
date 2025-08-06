package io.inventory_service.dtos.receipt;

import io.inventory_service.datatypes.ReceiptStatus;

import java.time.Instant;
import java.util.List;

public record StockReceiptCreateRequest(
        String receiptCode, // Human-readable unique ID for this receipt
        String warehouseId,
        String supplierId,
        String externalReference, // e.g., Supplier Invoice #, ASN ID
        Instant receiptTimestamp, // When the goods were physically received
        ReceiptStatus receiptStatus,
        String asnId, // Use the enum to indicate the current state of the receipt
        List<ReceiptItemDto> items

) {
}