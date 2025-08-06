package io.inventory_service.models;

import io.inventory_service.datatypes.ReorderPolicy;
import io.inventory_service.datatypes.ReviewFrequency;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Policy-as-data: tells the replenishment planner HOW and WHEN
 * to reorder a given SKU + variant at a specific warehouse.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("reorder_rule")
public class ReorderRule {

    /* ---------- identifiers ---------- */

    @Id
    private UUID id;                                     // surrogate PK

    @Column("rule_code")
    private String ruleCode;                             // e.g. “RR-ABC-1234-NBO”

    /* ---------- scope ---------- */

    @Column("sku")
    private String sku;

    @Column("variant_code")
    private String variantCode;

    @Column("warehouse_id")
    private String warehouseId;

    /* ---------- core policy ---------- */

    @Column("policy")
    private ReorderPolicy policy;                        // ROP | MIN_MAX | EOQ

    @Column("review_frequency")
    private ReviewFrequency reviewFrequency;             // CONTINUOUS | DAILY …

    /* ---------- classic parameters ---------- */

    @Column("safety_stock_units")
    private Integer safetyStockUnits;

    @Column("lead_time_days")
    private Integer leadTimeDays;

    @Column("average_daily_demand")
    private Integer averageDailyDemand;

    @Column("min_stock_units")
    private Integer minStockUnits;                       // MIN-MAX strategy

    @Column("max_stock_units")
    private Integer maxStockUnits;                       // MIN-MAX strategy

    @Column("reorder_point_units")
    private Integer reorderPointUnits;                   // pre-computed ROP

    @Column("reorder_quantity_units")
    private Integer reorderQuantityUnits;                // EOQ or fixed qty

    /* ---------- procurement constraints ---------- */

    @Column("min_order_quantity")
    private Integer minOrderQuantity;

    @Column("order_multiple")
    private Integer orderMultiple;                       // round up to X

    @Column("preferred_supplier_id")
    private String preferredSupplierId;

    /* ---------- activation window ---------- */

    @Column("active")
    private Boolean active;

    @Column("effective_from")
    private Instant effectiveFrom;

    @Column("effective_to")
    private Instant effectiveTo;

    /* ---------- extensibility ---------- */

    @Column("extra_params_json")
    private Json extraParams;                            // JSONB: service-level α etc.

    /* ---------- audit ---------- */

    @Column("created_at")
    private Instant createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("updated_by")
    private String updatedBy;

    /* ---------- helpers ---------- */

    public void onCreate(String userId) {
        this.createdAt = Instant.now();
        this.createdBy = userId;
        if (this.effectiveFrom == null) this.effectiveFrom = Instant.now();
        if (this.active == null) this.active = Boolean.TRUE;
    }

    public void onUpdate(String userId) {
        this.updatedAt = Instant.now();
        this.updatedBy = userId;
    }
}