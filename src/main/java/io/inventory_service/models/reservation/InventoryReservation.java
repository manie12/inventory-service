package io.inventory_service.models.reservation;

import io.inventory_service.datatypes.ReservationStatus;
import io.inventory_service.datatypes.ReservationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Transient;

import java.time.Instant;
import java.time.Duration; // For Time-To-Live
import java.util.UUID;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("reservations") // Maps to a database table named 'reservations'
public class InventoryReservation {

    @Id
    private UUID id; // Internal UUID for the entity

    @Column("reservation_code") // Human-readable/external ID (e.g., "RES-20250723-001")
    private String reservationCode;

    @Column("order_id") // ID of the sales order, work order, etc. that initiated this reservation
    private String orderId;

    @Column("reservation_type")
    private ReservationType reservationType;

    /** UTC timestamp when reservation was created. */

    @Column("requested_by_user_id")
    private String requestedByUserId;

    @Column("request_timestamp")
    private Instant requestTimestamp;

    @Column("expires_at")
    private Instant expiresAt; // The point in time when the reservation should expire

    @Column("reserved_at")
    private Instant reservedAt;
    @Column("status")
    private ReservationStatus reservationStatus;

    @Column("customer_id") // Applicable for SALES_ORDER type
    private String customerId;

    @Column("warehouse_id") // Which warehouse inventory is being reserved from
    private String warehouseId;

    @Column("notes")
    private String notes; // Any additional notes

    // Audit fields
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    // Relationships - child items are fetched/persisted separately in R2DBC
    @Transient
    private List<InventoryReservationItem> items;

    // Lifecycle callbacks
    public void onCreate() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.reservedAt = Instant.now();
    }

    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /**
     * Helper method to calculate expiresAt from requestTimestamp and timeToLive.
     * This might be called in the service layer before building the entity.
     * @param timeToLive The duration for which the reservation is valid.
     */
    public void calculateExpiresAt(Duration timeToLive) {
        if (this.requestTimestamp != null && timeToLive != null) {
            this.expiresAt = this.requestTimestamp.plus(timeToLive);
        } else if (this.requestTimestamp != null) {
            // Default TTL if not provided, or handle as per business rules
            this.expiresAt = this.requestTimestamp.plus(Duration.ofHours(1)); // Example default
        }
    }
}
