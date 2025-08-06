-- V10__create_reorder_rule_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.reorder_rule
(
    id                         UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    rule_code                  TEXT       NOT NULL UNIQUE,
    sku                        TEXT       NOT NULL,
    variant_code               TEXT,
    warehouse_id               TEXT       NOT NULL,
    policy                     TEXT       NOT NULL CHECK (policy IN ('ROP', 'MIN_MAX', 'EOQ')),
    review_frequency           TEXT       NOT NULL CHECK (review_frequency IN ('CONTINUOUS', 'DAILY', 'WEEKLY', 'MONTHLY')),
    safety_stock_units         INTEGER,
    lead_time_days             INTEGER,
    average_daily_demand       INTEGER,
    min_stock_units            INTEGER,
    max_stock_units            INTEGER,
    reorder_point_units        INTEGER,
    reorder_quantity_units     INTEGER,
    min_order_quantity         INTEGER,
    order_multiple             INTEGER,
    preferred_supplier_id      TEXT,
    active                     BOOLEAN    NOT NULL DEFAULT TRUE,
    effective_from             TIMESTAMPTZ NOT NULL,
    effective_to               TIMESTAMPTZ,
    extra_params_json          JSONB,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                 TEXT       NOT NULL,
    updated_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_by                 TEXT
    );

-- Indexes for efficient lookups
CREATE INDEX IF NOT EXISTS idx_reorder_rule_rule_code ON yapa.reorder_rule (rule_code);
CREATE INDEX IF NOT EXISTS idx_reorder_rule_sku_variant_wh ON yapa.reorder_rule (sku, variant_code, warehouse_id);
CREATE INDEX IF NOT EXISTS idx_reorder_rule_active ON yapa.reorder_rule (active);
CREATE INDEX IF NOT EXISTS idx_reorder_rule_effective ON yapa.reorder_rule (effective_from, effective_to);

-- Optional: Foreign keys to other tables
-- ALTER TABLE yapa.reorder_rule ADD CONSTRAINT fk_reorder_rule_sku FOREIGN KEY (sku) REFERENCES yapa.products (sku);
-- ALTER TABLE yapa.reorder_rule ADD CONSTRAINT fk_reorder_rule_variant FOREIGN KEY (variant_code) REFERENCES yapa.variants (code);
-- ALTER TABLE yapa.reorder_rule ADD CONSTRAINT fk_reorder_rule_warehouse FOREIGN KEY (warehouse_id) REFERENCES yapa.warehouses (id);
-- ALTER TABLE yapa.reorder_rule ADD CONSTRAINT fk_reorder_rule_supplier FOREIGN KEY (preferred_supplier_id) REFERENCES yapa.suppliers (id);