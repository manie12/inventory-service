package io.inventory_service.repo.customs;

import io.inventory_service.models.reservation.InventoryReservation;
import io.inventory_service.models.reservation.InventoryReservationItem;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * Extra behaviours beyond simple CRUD.
 */
public interface InventoryReservationRepositoryCustom {

    /**
     * Persist header + items in a single reactive transaction.
     */
    Mono<InventoryReservation> insertWithItems(InventoryReservation header,
                                               List<InventoryReservationItem> items);

    Mono<InventoryReservation> findByIdWithItems(UUID id);

    Mono<InventoryReservation> findByReservationCodeWithItems(String reservationCode);

    /**
     * Idempotent up-sert keyed by reservationCode
     * (handy if caller retries the same request).
     */
    Mono<InventoryReservation> upsertByCode(InventoryReservation header,
                                            List<InventoryReservationItem> items);
}