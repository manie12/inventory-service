package io.inventory_service.repo.release;

import io.inventory_service.models.release.InventoryReservationReleaseItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface InventoryReservationReleaseItemRepository
        extends ReactiveCrudRepository<InventoryReservationReleaseItem, Long> {

    Flux<InventoryReservationReleaseItem> findByReleaseId(UUID releaseId);
}