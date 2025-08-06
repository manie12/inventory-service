package io.inventory_service.models.transfer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Individual SKU / variant quantities being moved.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("inventory_transfer_item")
public class InventoryTransferItem {

    /**
     * Surrogate PK.
     */
    @Id
    private UUID id;

    /**
     * FK to header (UUID).
     */
    @Column("inventory_transfer_id") // Foreign key to StockTransfer
    private UUID inventoryTransferId; // Links to the parent StockTransfer's internal ID


    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    @Column("quantity")
    private Integer quantity;

    /**
     * Optional batch / lot / serial information.
     */
    @Column("batch_no")
    private String batchNo;

    @Column("source_location_id")
    private String sourceLocationId;

    @Column("destination_location_id")
    private String destinationLocationId;

    @Column("serial_no") // Optional: if specific serialized items are transferred
    private String serialNo;

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