package io.inventory_service.repo.customs;

import io.inventory_service.models.adjustment.InventoryAdjustmentItem;
import reactor.core.publisher.Flux;

import java.util.List;

public interface InventoryAdjustmentItemRepositoryCustom {
    Flux<InventoryAdjustmentItem> bulkInsert(List<InventoryAdjustmentItem> items);
}