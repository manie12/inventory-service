package io.inventory_service.repo.receipt;

import io.inventory_service.models.receipt.InventoryReceipt;
import io.inventory_service.repo.customs.InventoryReceiptRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * Header table.
 */
public interface
InventoryReceiptRepository
        extends ReactiveCrudRepository<InventoryReceipt, UUID>,
        InventoryReceiptRepositoryCustom {

    Mono<InventoryReceipt> findByReceiptCode(String receiptCode);

    Flux<InventoryReceipt> findByWarehouseIdAndReceivedAtBetween(
            String warehouseId, Instant from, Instant to);
}

