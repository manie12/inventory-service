package io.inventory_service.models.restock;

import io.inventory_service.datatypes.ReturnSource;
import io.inventory_service.datatypes.ReturnStatus;
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
@Table("inventory_return")
public class InventoryReturn {

    /**
     * Surrogate PK.
     */
    @Id
    private UUID id;

    /**
     * Business/idempotency key, e.g. "RET-ORD-123456".
     */
    @Column("return_code")
    private String returnCode;

    /**
     * Link back to the originating order/shipment (for customer) or PO (for supplier).
     */
    @Column("original_order_id")
    private String originalOrderId;

    @Column("original_shipment_id")
    private String originalShipmentId;

    @Column("purchase_order_id")
    private String purchaseOrderId;      // for supplier returns (nullable)

    @Column("rma_number")
    private String rmaNumber;            // Return Merchandise Authorization (optional)

    @Column("warehouse_id")
    private String warehouseId;

    @Column("return_source")
    private ReturnSource returnSource;         // CUSTOMER / SUPPLIER

    @Column("return_status")
    private ReturnStatus returnStatus;         // DRAFT / RECEIVED / INSPECTED / COMPLETED

    @Column("received_at")
    private Instant receivedAt;

    @Column("inspected_at")
    private Instant inspectedAt;

    @Column("returned_by_user_id") // Who initiated/recorded the return
    private String returnedByUserId;

    @Column("external_reference_id") // e.g., Original Sales Order ID, RMA Number, Supplier PO
    private String externalReferenceId;



    @Column("completed_at")
    private Instant completedAt;

    @Column("note")
    private String note;

    // Relationships - child items are fetched/persisted separately in R2DBC
    @Transient
    private List<InventoryReturnItem> items;


    /* --- audit --- */
    @Column("created_at")
    private Instant createdAt;
    @Column("created_by")
    private String createdBy;

    public void onCreate(String userId) {
        this.createdAt = Instant.now();
        this.createdBy = userId;
        if (this.returnStatus == null) this.returnStatus = ReturnStatus.DRAFT;
    }
}