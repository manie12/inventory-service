package io.inventory_service.repo.customs;

import io.inventory_service.models.restock.InventoryReturn;
import io.inventory_service.models.restock.InventoryReturnItem;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface InventoryReturnRepositoryCustom {

    Mono<InventoryReturn> insertWithItems(InventoryReturn header,
                                          List<InventoryReturnItem> items);

    Mono<InventoryReturn> upsertByCode(InventoryReturn header,
                                       List<InventoryReturnItem> items);

    /**
     * Finds a ReturnRestock by its internal UUID and eagerly fetches its associated items.
     *
     * @param id The internal UUID of the ReturnRestock.
     * @return A Mono emitting the ReturnRestock with its items, or empty if not found.
     */
    Mono<InventoryReturn> findByIdWithItems(UUID id);

    /**
     * Finds a ReturnRestock by its unique return code and eagerly fetches its associated items.
     *
     * @param returnCode The unique return code.
     * @return A Mono emitting the ReturnRestock with its items, or empty if not found.
     */
    Mono<InventoryReturn> findByReturnCodeWithItems(String returnCode);
}