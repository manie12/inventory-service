package io.inventory_service.service;

import io.inventory_service.dtos.receipt.StockReceiptCreateRequest;
import io.inventory_service.models.receipt.InventoryReceipt;
import reactor.core.publisher.Mono;

public interface StockReceiptService {
    Mono<InventoryReceipt> createReceipt(StockReceiptCreateRequest dto, String userId);
}
