-- V2__create_inventory_adjustment_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.inventory_adjustment
(
    id                        UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    adjustment_code           TEXT       NOT NULL UNIQUE, -- Business/idempotency key
    warehouse_id              TEXT       NOT NULL,
    location_id               TEXT,                       -- Optional
    reason_code               TEXT       NOT NULL CHECK (reason_code IN ('DAMAGE', 'LOSS', 'AUDIT', 'RETURN')),
    adjustment_status         TEXT       NOT NULL CHECK (adjustment_status IN ('DRAFT', 'POSTED', 'REVERSED')),
    adjusted_by_user_id       TEXT       NOT NULL,
    reference_document_id     TEXT,                       -- Optional
    adjusted_at               TIMESTAMPTZ NOT NULL,
    note                      TEXT,
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                TEXT       NOT NULL,
    UNIQUE (adjustment_code)
    );

-- Basic indexes for common fields
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_warehouse_id ON yapa.inventory_adjustment (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_status ON yapa.inventory_adjustment (adjustment_status);
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_adjusted_at ON yapa.inventory_adjustment (adjusted_at);
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_created_at ON yapa.inventory_adjustment (created_at);

-- Combined index for common query pattern
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_warehouse_status ON yapa.inventory_adjustment (warehouse_id, adjustment_status);
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_adjusted_by_user ON yapa.inventory_adjustment (adjusted_by_user_id);

-- If you have a separate table for warehouses, you may want to add a foreign key
-- ALTER TABLE yapa.inventory_adjustment ADD CONSTRAINT fk_inventory_adjustment_warehouse FOREIGN KEY (warehouse_id) REFERENCES yapa.warehouses (id) ON DELETE RESTRICT;

-- If you have a separate table for users, you may want to add a foreign key
-- ALTER TABLE yapa.inventory_adjustment ADD CONSTRAINT fk_inventory_adjustment_user FOREIGN KEY (adjusted_by_user_id) REFERENCES yapa.users (id) ON DELETE RESTRICT;
-- ALTER TABLE yapa.inventory_adjustment ADD CONSTRAINT fk_inventory_adjustment_created_by FOREIGN KEY (created_by) REFERENCES yapa.users (id) ON DELETE RESTRICT;