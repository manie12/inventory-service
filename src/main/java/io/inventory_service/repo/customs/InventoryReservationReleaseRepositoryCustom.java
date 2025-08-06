package io.inventory_service.repo.customs;

import io.inventory_service.models.release.InventoryReservationRelease;
import io.inventory_service.models.release.InventoryReservationReleaseItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface InventoryReservationReleaseRepositoryCustom {

    /**
     * Persist a release header together with all its items in one
     * reactive transaction.
     */
    Mono<InventoryReservationRelease> insertWithItems(InventoryReservationRelease header,
                                                      List<InventoryReservationReleaseItem> items);

    /**
     * Idempotent upsert by releaseCode (safe for client retries).
     */
    Mono<InventoryReservationRelease> upsertByCode(InventoryReservationRelease header,
                                                   List<InventoryReservationReleaseItem> items);

    /**
     * Finds a ReservationReleaseEvent by its internal UUID and eagerly fetches its associated items.
     *
     * @param id The internal UUID of the ReservationReleaseEvent.
     * @return A Mono emitting the ReservationReleaseEvent with its items, or empty if not found.
     */
    Mono<InventoryReservationRelease> findByIdWithItems(UUID id);

    /**
     * Finds a ReservationReleaseEvent by its unique release code and eagerly fetches its associated items.
     *
     * @param releaseCode The unique release code.
     * @return A Mono emitting the ReservationReleaseEvent with its items, or empty if not found.
     */
    Mono<InventoryReservationRelease> findByReleaseCodeWithItems(String releaseCode);

    /**
     * Finds all ReservationReleaseEvents associated with a specific Reservation ID.
     *
     * @param reservationId The UUID of the Reservation.
     * @return A Flux emitting ReservationReleaseEvent entities.
     */
    Flux<InventoryReservationRelease> findByReservationId(UUID reservationId);

}