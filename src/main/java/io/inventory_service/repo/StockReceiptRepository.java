package io.inventory_service.repo;

import io.inventory_service.models.StockReceipt;
import io.inventory_service.models.StockReceiptItem;
import io.inventory_service.repo.customs.StockReceiptRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

/**
 * Header table.
 */
public interface StockReceiptRepository
        extends ReactiveCrudRepository<StockReceipt, UUID>,
        StockReceiptRepositoryCustom {

    Mono<StockReceipt> findByReceiptCode(String receiptCode);

    Flux<StockReceipt> findByWarehouseIdAndReceivedAtBetween(
            String warehouseId, Instant from, Instant to);
}

