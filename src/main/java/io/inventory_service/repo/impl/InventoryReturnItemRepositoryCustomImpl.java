package io.inventory_service.repo.impl;

import io.inventory_service.models.restock.InventoryReturnItem;
import io.inventory_service.repo.customs.InventoryReturnItemRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryReturnItemRepositoryCustomImpl
        implements InventoryReturnItemRepositoryCustom {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<InventoryReturnItem> bulkInsert(List<InventoryReturnItem> items) {
        return Flux.fromIterable(items)
                .flatMap(i -> template.insert(InventoryReturnItem.class).using(i));
    }
}