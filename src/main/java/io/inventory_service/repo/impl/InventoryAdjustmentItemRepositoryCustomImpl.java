package io.inventory_service.repo.impl;

import io.inventory_service.models.adjustment.InventoryAdjustmentItem;
import io.inventory_service.repo.customs.InventoryAdjustmentItemRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryAdjustmentItemRepositoryCustomImpl
        implements InventoryAdjustmentItemRepositoryCustom {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<InventoryAdjustmentItem> bulkInsert(List<InventoryAdjustmentItem> items) {
        return Flux.fromIterable(items)
                .flatMap(i -> template.insert(InventoryAdjustmentItem.class).using(i));
    }
}