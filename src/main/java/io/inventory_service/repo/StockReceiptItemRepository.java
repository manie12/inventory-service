package io.inventory_service.repo;

import io.inventory_service.models.StockReceiptItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * Line table (simple CRUD is enough).
 */
public interface StockReceiptItemRepository
        extends ReactiveCrudRepository<StockReceiptItem, Long> {

    Flux<StockReceiptItem> findByReceiptId(UUID receiptId);
}