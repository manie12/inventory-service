package io.inventory_service.repo.reservation;

import io.inventory_service.models.reservation.InventoryReservation;
import io.inventory_service.repo.customs.InventoryReservationRepositoryCustom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface InventoryReservationRepository
        extends ReactiveCrudRepository<InventoryReservation, UUID>,
        InventoryReservationRepositoryCustom {

    Mono<InventoryReservation> findByReservationCode(String reservationCode);

    Flux<InventoryReservation> findByReservationId(UUID reservationId);

    Flux<InventoryReservation> findByWarehouseIdAndReservedAtBetween(
            String warehouseId, Instant from, Instant to);


}