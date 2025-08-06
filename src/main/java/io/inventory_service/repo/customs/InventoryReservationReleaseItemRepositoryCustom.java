package io.inventory_service.repo.customs;

import io.inventory_service.models.release.InventoryReservationReleaseItem;
import reactor.core.publisher.Flux;

import java.util.List;

public interface InventoryReservationReleaseItemRepositoryCustom {

    Flux<InventoryReservationReleaseItem> bulkInsert(List<InventoryReservationReleaseItem> items);
}