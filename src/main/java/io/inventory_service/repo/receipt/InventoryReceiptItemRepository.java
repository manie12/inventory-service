package io.inventory_service.repo.receipt;

import io.inventory_service.models.receipt.InventoryReceiptItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Line table (simple CRUD is enough).
 */
public interface InventoryReceiptItemRepository
        extends ReactiveCrudRepository<InventoryReceiptItem, Long> {

    Flux<InventoryReceiptItem> findByReceiptId(UUID receiptId);
}