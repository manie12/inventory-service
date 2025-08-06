package io.inventory_service.dtos.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/* POST /reservations */
public record ReservationCreateRequest(
        @NotBlank(message = "Reservation code must not be blank")
        String reservationCode, // Human-readable unique ID for this reservation

        @NotBlank(message = "Order ID must not be blank")
        String orderId,         // ID of the sales order, work order etc.

        @NotNull(message = "Reservation type must be specified")
        String type,            // Will be mapped to ReservationType enum (e.g., "SALES_ORDER")

        @NotBlank(message = "Requested by user ID must not be blank")
        String requestedByUserId,

        @NotNull(message = "Request timestamp must not be null")
        Instant requestTimestamp,

        // Duration for which the reservation is valid (e.g., "PT1H" for 1 hour)
        // If null, service might apply a default TTL
        Duration timeToLive,

        @NotBlank(message = "Warehouse ID must not be blank")
        String warehouseId,

        String customerId, // Optional: for sales orders

        @Size(min = 1, message = "Reservation must contain at least one item")
        @Valid // Ensures validation is applied to elements within the list
        List<ReservationItemDto> items,

        String notes


) {
}
