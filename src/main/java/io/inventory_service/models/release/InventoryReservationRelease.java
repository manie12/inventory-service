package io.inventory_service.models.release;

import io.inventory_service.datatypes.ReleaseAction;
import io.inventory_service.datatypes.ReleaseReason;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Immutable audit record capturing a single reservation release event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_reservation_release")
public class InventoryReservationRelease {

    /**
     * Surrogate PK.
     */
    @Id
    private UUID id;

    /**
     * Business idempotency key, e.g. "REL-RES-123456-1".
     */
    @Column("release_code")
    private String releaseCode;

    /**
     * The reservation being released.
     */
    @Column("reservation_id")
    private UUID reservationId;

    /**
     * Mirrors reservation/order linkage for easy reporting.
     */
    @Column("reservation_code")
    private String reservationCode;

    @Column("release_timestamp")
    private Instant releaseTimestamp; // When the release action occurred

    @Column("order_id")
    private String orderId;

    @Column("warehouse_id")
    private String warehouseId;

    @Column("release_type")
    private ReleaseAction releaseAction;   // FULFILLED, CANCELLED, EXPIRED

    @Column("release_reason")
    private ReleaseReason releaseReason; // optional extra semantics

    @Column("released_at")
    private Instant releasedAt;

    @Column("released_by")
    private String releasedBy;

    @Column("notes")
    private String notes;

    @Column("shipment_id") // Optional: If this release is part of a larger shipment
    private String shipmentId;

    // Child items (specific items released)
    @Transient
    private List<InventoryReservationReleaseItem> items;

    /* --- audit --- */
    @Column("created_at")
    private Instant createdAt;

    @Column("created_by")
    private String createdBy;

    public void onCreate(String userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.createdBy = userId;
        if (this.releasedAt == null) this.releasedAt = Instant.now();
    }
}