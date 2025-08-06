package io.inventory_service.repo.transfer;

import io.inventory_service.models.transfer.InventoryTransfer;
import io.inventory_service.repo.customs.InventoryTransferRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface InventoryTransferRepository
        extends ReactiveCrudRepository<InventoryTransfer, UUID>,
        InventoryTransferRepositoryCustom {

    Mono<InventoryTransfer> findByTransferCode(String transferCode);

    Flux<InventoryTransfer> findBySourceWarehouseIdAndTransferTimestampBetween(
            String sourceWarehouseId, Instant from, Instant to);
}