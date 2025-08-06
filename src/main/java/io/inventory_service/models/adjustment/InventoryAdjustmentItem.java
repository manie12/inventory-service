package io.inventory_service.models.adjustment;

import lombok.Data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_adjustment_item")
public class InventoryAdjustmentItem {

    @Id
    private UUID id;

    @Column("adjustment_id")
    private UUID adjustmentId;

    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    @Column("quantity_change")
    private Integer quantityChange; // The change in quantity: can be positive (add) or negative (subtract)

    @Column("old_quantity_on_hand") // Optional: quantity before adjustment for audit
    private Integer oldQuantityOnHand;

    @Column("new_quantity_on_hand") // Optional: quantity after adjustment for audit
    private Integer newQuantityOnHand;

    @Column("serial_no") // Optional: if specific serialized items are adjusted
    private String serialNo;

    @Column("item_notes") // Notes specific to this line item
    private String itemNotes;

    /**
     * Quantity delta to apply (can be negative).
     * Example: -2 for shrinkage, +5 for data correction.
     */
    @Column("delta_qty")
    private Integer deltaQty;

    /**
     * (Optional) Per-unit cost change in minor units if you support
     * inventory valuation corrections. Otherwise omit.
     */
    @Column("unit_cost_minor")
    private Long unitCostMinor;

    /**
     * Optional batch/lot/serial targeting.
     */
    @Column("batch_no")
    private String batchNo;

    @Column("location_id")
    private String locationId;
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