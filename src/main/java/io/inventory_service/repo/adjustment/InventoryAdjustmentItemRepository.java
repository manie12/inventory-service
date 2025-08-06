package io.inventory_service.repo.adjustment;

import io.inventory_service.models.adjustment.InventoryAdjustmentItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface InventoryAdjustmentItemRepository
        extends ReactiveCrudRepository<InventoryAdjustmentItem, Long> {

    Flux<InventoryAdjustmentItem> findByAdjustmentId(UUID adjustmentId);
}