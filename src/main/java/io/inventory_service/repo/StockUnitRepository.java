package io.inventory_service.repo;

import io.inventory_service.models.StockUnit;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface StockUnitRepository extends ReactiveCrudRepository<StockUnit, UUID> {

    // Reserve N “free” units – FEFO and SKIP LOCKED to avoid contention
    @Query("""
           UPDATE stock_units
              SET state = 'RESERVED', updated_at = now()
            WHERE id IN (
                 SELECT id
                   FROM stock_units
                  WHERE tenant_id = :tenant
                    AND sku       = :sku
                    AND location  = :loc
                    AND state     = 'ON_HAND'
                  ORDER BY expiry_date NULLS LAST
                  LIMIT :qty
                  FOR UPDATE SKIP LOCKED)
           RETURNING id
           """)
    Flux<UUID> reserveUnitIds(UUID tenant, String sku, String loc, int qty);

    @Query("""
           UPDATE stock_units
              SET state = 'ON_HAND', updated_at = now()
            WHERE tenant_id = :tenant
              AND sku       = :sku
              AND location  = :loc
              AND state     = 'RESERVED'
              AND id = ANY(:unitIds)
           RETURNING id
           """)
    Flux<UUID> releaseUnitIds(UUID tenant, String sku, String loc, List<UUID> unitIds);
}