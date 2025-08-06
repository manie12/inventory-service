package io.inventory_service.repo.customs;

import io.inventory_service.models.receipt.InventoryReceipt;
import io.inventory_service.models.receipt.InventoryReceiptItem;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Extra behaviours that go beyond simple CRUD.
 */
public interface InventoryReceiptRepositoryCustom {

    /**
     * Persist a receipt header together with all its lines in a single
     * reactive transaction and return the saved header (with generated ID).
     */
    Mono<InventoryReceipt> insertWithItems(InventoryReceipt header,
                                           List<InventoryReceiptItem> items);

    /**
     * Idempotent up-sert by business code (useful if the caller retries).
     */
    Mono<InventoryReceipt> upsertById(InventoryReceipt header,
                                      List<InventoryReceiptItem> items);
}