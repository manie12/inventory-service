package io.inventory_service.repo.customs;

import io.inventory_service.models.restock.InventoryReturnItem;
import reactor.core.publisher.Flux;

import java.util.List;

public interface InventoryReturnItemRepositoryCustom {
    Flux<InventoryReturnItem> bulkInsert(List<InventoryReturnItem> items);
}