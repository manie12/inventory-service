package io.inventory_service.repo;

import io.inventory_service.models.StockItem;
import io.inventory_service.models.StockItem;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StockItemRepository extends ReactiveCrudRepository<StockItem, UUID> {

    Mono<StockItem> findByTenantIdAndSkuAndLocation(UUID tenantId, String sku, String location);

    @Modifying
    @Query("""
        UPDATE yapa.stock_items
           SET reserved = reserved + :qty,
               version  = version + 1,
               updated_at = NOW()
         WHERE id = :id
           AND (on_hand - reserved) >= :qty
        """)
    Mono<Integer> tryReserve(UUID id, int qty);

    @Modifying
    @Query("""
        UPDATE yapa.stock_items
           SET reserved = reserved - :qty,
               version  = version + 1,
               updated_at = NOW()
         WHERE id = :id
           AND reserved >= :qty
        """)
    Mono<Integer> releaseReserve(UUID id, int qty);
}