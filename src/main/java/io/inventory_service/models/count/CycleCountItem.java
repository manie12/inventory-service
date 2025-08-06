package io.inventory_service.models.count;

import io.inventory_service.datatypes.CountStatus;
import io.inventory_service.datatypes.CountVarianceResultStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * One SKU/variant (optionally per bin) that was counted in the session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("cycle_count_line")
public class CycleCountItem {

    @Id
    private UUID id;

    @Column("cycle_session_id")
    private UUID cycleSessionId;

    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    @Column("location_id")
    private String locationId;            // bin where the item was counted

    /**
     * Snapshot of the system quantity at the moment counting started.
     */
    @Column("system_quantity")
    private Integer systemQuantity; // Quantity expected by the system at the time of count initiation

    /**
     * What the counter actually found.
     */
    @Column("counted_quantity")
    private Integer countedQuantity; // Actual physical quantity found during the count

    @Column("discrepancy")
    private Integer discrepancy; // counted_quantity - system_quantity

    /**
     * Convenience delta = counted - system.
     */
    @Column("delta_qty")
    private Integer deltaQty;

    @Column("count_result_variance_type")
    private CountVarianceResultStatus countVarianceResultStatus;    // MATCH / SHORTAGE / OVERAGE

    @Column("count_status")
    private CountStatus countStatus;  // PENDING / COUNTED / APPROVED ...

    /**
     * Optional: link to the adjustment item created to fix this variance.
     */
    @Column("adjustment_item_id")
    private Long adjustmentItemId;

    @Column("batch_no") // Optional: if specific batches are counted
    private String batchNo;

    @Column("serial_no") // Optional: if specific serialized items are counted
    private String serialNo;

    @Column("item_notes") // Notes specific to this line item (e.g., "damaged box", "found misplaced")
    private String itemNotes;

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