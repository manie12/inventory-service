package io.inventory_service.repo.release;

import io.inventory_service.models.release.InventoryReservationRelease;
import io.inventory_service.repo.customs.InventoryReservationReleaseRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InventoryReservationReleaseRepository extends ReactiveCrudRepository<InventoryReservationRelease, UUID>,
        InventoryReservationReleaseRepositoryCustom {

    Mono<InventoryReservationRelease> findByReleaseCode(String releaseCode);

    Flux<InventoryReservationRelease> findByReservationId(UUID reservationId);
}