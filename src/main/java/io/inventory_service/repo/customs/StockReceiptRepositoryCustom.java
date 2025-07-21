package io.inventory_service.repo.customs;

import io.inventory_service.models.StockReceipt;
import io.inventory_service.models.StockReceiptItem;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Extra behaviours that go beyond simple CRUD.
 */
public interface StockReceiptRepositoryCustom {

    /**
     * Persist a receipt header together with all its lines in a single
     * reactive transaction and return the saved header (with generated ID).
     */
    Mono<StockReceipt> insertWithItems(StockReceipt header,
                                       List<StockReceiptItem> items);

    /**
     * Idempotent up-sert by business code (useful if the caller retries).
     */
    Mono<StockReceipt> upsertById(StockReceipt header,
                                    List<StockReceiptItem> items);
}