package io.inventory_service.models.restock;

import io.inventory_service.datatypes.ReturnCondition;
import io.inventory_service.datatypes.ReturnDisposition;
import io.inventory_service.datatypes.ReturnReason;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_return_item")
public class InventoryReturnItem {

    @Id
    private UUID id;

    @Column("return_id")
    private UUID returnRestockId;

    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    @Column("returned_quantity")
    private Integer returnedQuantity;

    @Column("return_condition")
    private ReturnCondition returnCondition;       // UNOPENED / OPENED / USED / DAMAGED

    @Column("return_disposition")
    private ReturnDisposition returnDisposition;   // RESTOCK / REFURBISH / SCRAP

    @Column("return_reason")
    private ReturnReason reason;             // DEFECTIVE / WRONG_ITEM_SENT / ...

    /** If you track inventory valuation on returns. */
    @Column("unit_cost_minor")
    private Long unitCostMinor;

    @Column("location_id") // Specific bin/shelf within the warehouse where it was restocked
    private String locationId;

    @Column("inspection_status") // e.g., "PASS", "FAIL", "REQUIRES_REPAIR"
    private String inspectionStatus; // String here, might map to a more specific enum/model

    @Column("serial_no") // Optional: if specific serialized items are returned
    private String serialNo;

    /** If refunds are computed per line. */
    @Column("refund_amount_minor")
    private Long refundAmountMinor;

    /** Where restocked/refurbished items should go. */
    @Column("restocked_location_id")
    private String restockedLocationId;

    /** Optional additional targeting info. */
    @Column("batch_no")
    private String batchNo;

    @Column("expiry_date")
    private LocalDate expiryDate;

    @Column("item_notes") // Notes specific to this line item
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