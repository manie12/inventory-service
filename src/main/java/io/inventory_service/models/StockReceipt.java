package io.inventory_service.models;

import io.inventory_service.datatypes.ReceiptStatus;
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
import org.springframework.data.annotation.Transient; // For fields not mapped directly to a column

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Useful for creating instances easily
@Table("stock_receipts") // Maps to a database table named 'stock_receipts'
public class StockReceipt {

    @Id // Marks this field as the primary key
    private UUID id; // Internal UUID for the entity

    @Column("receipt_id") // Maps to a column named 'receipt_id'
    private String receiptId; // The human-readable ID (e.g., "REC-20250717-001")

    @Column("warehouse_id")
    private String warehouseId;

    @Column("received_by_user_id")
    private String receivedByUserId;

    @Column("receipt_timestamp")
    private Instant receiptTimestamp; // Using Instant for precise UTC timestamps

    @Column("status")
    private ReceiptStatus status; // Enum for status (see below)

    @Column("external_reference_id")
    private String externalReferenceId; // e.g., Supplier Invoice #, Shipping #

    // Audit fields (common in enterprise applications)
    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    // Relationships (will be handled by separate queries or join tables if complex)
    // For simplicity with R2DBC, child entities are often fetched/persisted separately
//    @Transient // This field is not directly mapped to a database column in 'stock_receipts' table
//    private List<StockReceiptItem> items; // Items associated with this receipt

    // Lifecycle callbacks for auditing
    // @PrePersist and @PreUpdate are JPA annotations, in R2DBC you might handle this in service layer
    // or use database triggers/listeners. For simplicity, setting it manually or via aspect.
    public void onCreate() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}