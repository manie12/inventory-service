package io.inventory_service.repo;

import io.inventory_service.dtos.BackOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface BackOrderRepository
        extends ReactiveCrudRepository<BackOrder, UUID> {

    /**
     * Oldest first for fair allocation
     */
    Flux<BackOrder> findBySkuAndLocationOrderByQueuedAtAsc(String sku, String location);
}