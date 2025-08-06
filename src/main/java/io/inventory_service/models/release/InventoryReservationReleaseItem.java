package io.inventory_service.models.release;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Line-level quantities released from a reservation.
 * Lets you partially release / fulfill a reservation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_reservation_release_item")
public class InventoryReservationReleaseItem {

    @Id
    private UUID id;

    @Column("release_id")
    private UUID releaseId;

    /**
     * Optionally link back to the specific reservation item row.
     */
    @Column("reservation_item_id")
    private Long reservationItemId;

    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    /**
     * Quantity released (can be <= reserved to support partials).
     */
    @Column("quantity_released")
    private Integer quantityReleased;

    @Column("picked_from_location_id") // For fulfillment: where was it physically picked from?
    private String pickedFromLocationId;

    @Column("batch_no") // For fulfillment: which batch was picked?
    private String batchNo;

    /**
     * Optional outbound link (shipment / fulfillment doc).
     */
    @Column("shipment_id")
    private String shipmentId;
    @Column("created_at")
    private Instant createdAt;

    @Column("created_by")
    private String createdBy;

    public void onCreate(String userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.createdBy = userId;
    }

}