package io.inventory_service.models.count;

import io.inventory_service.datatypes.CountMethod;
import io.inventory_service.datatypes.CountType;
import io.inventory_service.datatypes.CycleCountStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * One cycle-count session (could be a bin, an ABC class, or entire WH).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("cycle_count_session")
public class CycleCountSession {

    @Id
    private UUID id;

    /** Business/idempotency key, e.g. "CCS-2025-07-24-0005". */
    @Column("cycle_session_id")
    private String cycleSessionCode;

    @Column("warehouse_id")
    private String warehouseId;

    /** Optional: narrow to a specific bin/location for this session. */
    @Column("location_id")
    private String locationId;

    @Column("location_scope") // Defines the scope of the count (e.g., "AISLE_A", "BIN_123", "ALL_A_ITEMS")
    private String locationScope; // Could also be a more complex object/JSON or another entity relationship

    @Column("count_method")
    private CountMethod countMethod;               // CYCLE / FULL / SPOT

    @Column("cyscle_status")
    private CycleCountStatus cycleCountStatus;          // PLANNED / IN_PROGRESS / ...

    @Column("count_type")
    private CountType countType; // ADHOC, SCHEDULED, BLIND, VERIFICATION


    @Column("counted_by_user_id") // Who performed the count
    private String countedByUserId;

    @Column("approved_by_user_id") // Who approved the count/adjustments
    private String approvedByUserId;

    @Column("approval_date")
    private Instant approvalDate;

    @Column("scheduled_at")
    private Instant scheduledAt;

    @Column("started_at")
    private Instant startedAt;

    @Column("completed_at")
    private Instant completedAt;

    /** If you automatically raise stock adjustments afterwards, link them. */
    @Column("adjustment_id")
    private UUID adjustmentId;                // nullable

    @Column("notes")
    private String notes; // Any additional notes about the count
    // Relationships - child items are fetched/persisted separately in R2DBC

    @Transient
    private List<CycleCountItem> items;

    /* --- audit --- */
    @Column("created_at")
    private Instant createdAt;

    @Column("created_by")
    private String createdBy;

    public void onCreate(String userId) {
        this.createdAt = Instant.now();
        this.createdBy = userId;
        if (this.cycleCountStatus == null) this.cycleCountStatus = CycleCountStatus.PLANNED;
    }

    public void onStart() {
        this.startedAt = Instant.now();
        this.cycleCountStatus = CycleCountStatus.IN_PROGRESS;
    }

    public void onComplete() {
        this.completedAt = Instant.now();
        this.cycleCountStatus = CycleCountStatus.COMPLETED;
    }
}