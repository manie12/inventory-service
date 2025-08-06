package io.inventory_service.models.receipt;

import io.inventory_service.datatypes.InspectionResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
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
@Table("stock_receipt_items") // Maps to a database table named 'stock_receipt_items'
public class InventoryReceiptItem {

    @Id
    private UUID id;

    @Column("receipt_id") // Foreign key to StockReceipt
    private UUID receiptId; // Links to the parent StockReceipt's internal ID

    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    @Column("quantity_received")
    private Integer quantityReceived; // Using Integer for quantity

    @Column("purchase_order_id")
    private String purchaseOrderId;

    @Column("asn_id")
    private String asnId;

    @Column("supplier_id")
    private String supplierId;

    @Column("unit_cost")
    private BigDecimal unitCost; // Use BigDecimal for currency precision

    @Column("target_location_id")
    private String targetLocationId;

    @Column("inspection_result")
    private InspectionResult inspectionResult; // Enum for inspection result (see below)

    @Column("batch_number")
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