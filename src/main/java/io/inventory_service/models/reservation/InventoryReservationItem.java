package io.inventory_service.models.reservation;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("reservation_items") // Maps to a database table named 'reservation_items'
public class InventoryReservationItem {

    @Id
    private UUID id;

    @Column("reservation_id") // Foreign key to Reservation
    private UUID reservationId; // Links to the parent Reservation's internal ID

    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    @Column("quantity")
    private Integer quantity; // Quantity being reserved

    @Column("preferred_location_id") // If a specific location is preferred for picking
    private String preferredLocationId;

    @Column("batch_no") // Optional: if specific batches are required for allocation (e.g., expiry date specific)
    private String batchNo;

    // Audit fields
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    public void onCreate() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
