package io.inventory_service.models.adjustment;

import io.inventory_service.datatypes.AdjustmentReasonCode;
import io.inventory_service.datatypes.AdjustmentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_adjustment")
public class InventoryAdjustment {

    /**
     * Surrogate PK.
     */
    @Id
    private UUID id;

    /**
     * Business/idempotency key, e.g. "ADJ-2025-07-24-0007".
     */
    @Column("adjustment_code")
    private String adjustmentCode;

    @Column("warehouse_id")
    private String warehouseId;

    /**
     * Optional finer location/bin.
     */
    @Column("location_id")
    private String locationId;

    @Column("reason")
    private AdjustmentReasonCode reasonCode;

    @Column("status")
    private AdjustmentStatus adjustmentStatus;          // DRAFT / POSTED / REVERSED

    /**
     * Who performed the physical count / correction.
     */
    @Column("adjusted_by_user_id") // Who initiated/recorded the adjustment
    private String adjustedByUserId;

    @Column("reference_document_id") // Optional: e.g., Physical Count document ID, QA Report ID
    private String referenceDocumentId;

    @Column("adjusted_at")
    private Instant adjustedAt;

    @Column("note")
    private String note;

    // Relationships - child items are fetched/persisted separately in R2DBC
    @Transient
    private List<InventoryAdjustmentItem> items;

    /* --- audit --- */
    @Column("created_at")
    private Instant createdAt;

    @Column("created_by")
    private String createdBy;

    public void onCreate(String userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.createdBy = userId;
        if (this.adjustedAt == null) this.adjustedAt = Instant.now();
        if (this.adjustmentStatus == null) this.adjustmentStatus = AdjustmentStatus.DRAFT;
    }
}