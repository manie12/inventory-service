package io.inventory_service.service;

import io.inventory_service.dtos.ReserveRequest;
import io.inventory_service.dtos.reservation.ReservationCreateRequest;
import io.inventory_service.models.reservation.InventoryReservation;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReservationService {

    Mono<Void> releaseReservation(String reservationCode,
                                  InReservationReleaseRequest req, String userId);
    /**
     * Creates a new inventory reservation.
     * This operation attempts to soft-allocate inventory and is transactional.
     *
     * @param request The DTO containing reservation details.
     * @return A Mono emitting the created Reservation entity.
     * @throws IllegalArgumentException     if input data is invalid.
//     * @throws InsufficientStockException   if not enough stock is available for reservation.
//     * @throws ReservationConflictException if a reservation with the same code already exists.
//     * @throws InvalidProductException      if an SKU/variant is not recognized.
     */
    Mono<ReservationCreateRequest> createReservation(ReservationCreateRequest request);

    /**
     * Retrieves a reservation by its unique internal ID, including its items.
     *
     * @param id The internal UUID of the reservation.
     * @return A Mono emitting the Reservation with items, or empty if not found.
     */
    Mono<InventoryReservation> getReservationById(UUID id);

    /**
     * Retrieves a reservation by its human-readable code, including its items.
     *
     * @param reservationCode The human-readable code of the reservation.
     * @return A Mono emitting the Reservation with items, or empty if not found.
     */
    Mono<InventoryReservation> getReservationByCode(String reservationCode);

    // Potentially other methods:
    // Mono<Reservation> updateReservation(String reservationCode, ReservationUpdateRequest request);
    // Mono<Void> cancelReservation(String reservationCode);
    // Mono<Void> fulfillReservation(String reservationCode, List<FulfillmentItemDto> items);
    // Flux<Reservation> findReservationsByOrderId(String orderId);
}
