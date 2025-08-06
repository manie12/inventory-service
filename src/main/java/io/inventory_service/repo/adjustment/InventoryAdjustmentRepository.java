package io.inventory_service.repo.adjustment;

import io.inventory_service.datatypes.AdjustmentStatus;
import io.inventory_service.models.adjustment.InventoryAdjustment;
import io.inventory_service.repo.customs.InventoryAdjustmentRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface InventoryAdjustmentRepository
        extends ReactiveCrudRepository<InventoryAdjustment, UUID>,
        InventoryAdjustmentRepositoryCustom {

    Mono<InventoryAdjustment> findByAdjustmentCode(String adjustmentCode);

    Flux<InventoryAdjustment> findByWarehouseIdAndAdjustedAtBetween(
            String warehouseId, Instant from, Instant to);

    Flux<InventoryAdjustment> findByStatus(AdjustmentStatus status);
}