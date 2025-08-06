package io.inventory_service.dtos.reservation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/* One line in the reservation */
public record ReservationItemDto(
        @NotBlank(message = "SKU must not be blank")
        String sku,
        String variantCode, // Optional: if product has no variants, or if default variant
        // is implied. Should be validated against product service.
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,
        String preferredLocationId, // Optional: if reservation specifies a preferred pick location
        String batchNo // Optional: if a specific batch is required

) {
}