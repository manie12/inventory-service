package io.inventory_service.repo.transfer;

import io.inventory_service.models.transfer.InventoryTransferItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface InventoryTransferItemRepository
        extends ReactiveCrudRepository<InventoryTransferItem, Long> {

    Flux<InventoryTransferItem> findByInventoryTransferId(UUID transferId);
}