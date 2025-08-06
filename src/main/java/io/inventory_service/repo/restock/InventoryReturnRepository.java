package io.inventory_service.repo.restock;

import io.inventory_service.datatypes.ReturnStatus;
import io.inventory_service.models.restock.InventoryReturn;
import io.inventory_service.repo.customs.InventoryReturnRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface InventoryReturnRepository
        extends ReactiveCrudRepository<InventoryReturn, UUID>,
        InventoryReturnRepositoryCustom {

    Mono<InventoryReturn> findByReturnCode(String returnCode);

    Flux<InventoryReturn> findByWarehouseIdAndReceivedAtBetween(
            String warehouseId, Instant from, Instant to);

    Flux<InventoryReturn> findByStatus(ReturnStatus status);
}