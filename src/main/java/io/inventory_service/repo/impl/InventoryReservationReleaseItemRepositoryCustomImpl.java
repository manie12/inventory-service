package io.inventory_service.repo.impl;

import io.inventory_service.models.release.InventoryReservationReleaseItem;
import io.inventory_service.repo.customs.InventoryReservationReleaseItemRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class InventoryReservationReleaseItemRepositoryCustomImpl
        implements InventoryReservationReleaseItemRepositoryCustom {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<InventoryReservationReleaseItem> bulkInsert(List<InventoryReservationReleaseItem> items) {
        return Flux.fromIterable(items)
                .flatMap(i -> template.insert(InventoryReservationReleaseItem.class).using(i));
    }
}