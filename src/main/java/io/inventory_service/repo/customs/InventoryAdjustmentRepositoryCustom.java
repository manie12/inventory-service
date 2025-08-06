package io.inventory_service.repo.customs;


import io.inventory_service.models.adjustment.InventoryAdjustment;
import io.inventory_service.models.adjustment.InventoryAdjustmentItem;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface InventoryAdjustmentRepositoryCustom {

    /**
     * Persist header + items atomically in a single reactive transaction.
     */
    Mono<InventoryAdjustment> insertWithItems(InventoryAdjustment header,
                                              List<InventoryAdjustmentItem> items);

    /**
     * Idempotent upsert keyed by adjustmentCode (safe for retries).
     */
    Mono<InventoryAdjustment> upsertByCode(InventoryAdjustment header,
                                           List<InventoryAdjustmentItem> items);


    /**
     * Finds a StockAdjustment by its internal UUID and eagerly fetches its associated items.
     *
     * @param id The internal UUID of the StockAdjustment.
     * @return A Mono emitting the StockAdjustment with its items, or empty if not found.
     */
    Mono<InventoryAdjustment> findByIdWithItems(UUID id);

    /**
     * Finds a StockAdjustment by its unique adjustment code and eagerly fetches its associated items.
     *
     * @param adjustmentCode The unique adjustment code.
     * @return A Mono emitting the StockAdjustment with its items, or empty if not found.
     */
    Mono<InventoryAdjustment> findByAdjustmentCodeWithItems(String adjustmentCode);
}