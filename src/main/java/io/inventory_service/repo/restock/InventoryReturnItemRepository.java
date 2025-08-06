package io.inventory_service.repo.restock;

import io.inventory_service.models.restock.InventoryReturnItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface InventoryReturnItemRepository extends ReactiveCrudRepository<InventoryReturnItem, Long> {

    Flux<InventoryReturnItem> findByReturnId(UUID returnId);
}