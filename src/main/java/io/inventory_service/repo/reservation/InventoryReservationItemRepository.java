package io.inventory_service.repo.reservation;

import io.inventory_service.models.reservation.InventoryReservationItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface InventoryReservationItemRepository
        extends ReactiveCrudRepository<InventoryReservationItem, Long> {

    Flux<InventoryReservationItem> findByReservationId(UUID reservationId);
}