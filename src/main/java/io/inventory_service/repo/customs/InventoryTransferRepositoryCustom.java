package io.inventory_service.repo.customs;

import io.inventory_service.models.transfer.InventoryTransfer;
import io.inventory_service.models.transfer.InventoryTransferItem;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * Extra behaviours that go beyond simple CRUD.
 */
public interface InventoryTransferRepositoryCustom {

    /**
     * Persist a transfer header together with all its item rows
     * in a single reactive transaction.
     */
    Mono<InventoryTransfer> insertWithItems(InventoryTransfer header,
                                            List<InventoryTransferItem> items);

    /**
     * Idempotent up-sert by business code (transferCode).
     * Useful if the caller retries the same request.
     */
    Mono<InventoryTransfer> upsertByCode(InventoryTransfer header,
                                         List<InventoryTransferItem> items);
}