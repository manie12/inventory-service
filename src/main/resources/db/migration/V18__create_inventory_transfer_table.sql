-- V15__create_inventory_transfer_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.inventory_transfer
(
    id                         UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    transfer_code              TEXT       NOT NULL UNIQUE,
    source_warehouse_id        TEXT       NOT NULL,
    destination_warehouse_id   TEXT       NOT NULL,
    requested_by_user_id       TEXT,
    source_location_id         TEXT,
    destination_location_id    TEXT,
    approver_user_id           TEXT,
    requested_by               TEXT,
    transfer_timestamp         TIMESTAMPTZ NOT NULL,
    reason                     TEXT       NOT NULL CHECK (reason IN ('REBALANCE', 'STORE_REPLENISH', 'QUALITY_CONTROL', 'DAMAGED_ITEMS')),
    status                     TEXT       NOT NULL CHECK (status IN ('DRAFT', 'IN_TRANSIT', 'COMPLETED', 'CANCELLED')),
    notes                      TEXT,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                 TEXT       NOT NULL,
    updated_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- Indexes for common lookups and foreign keys
CREATE INDEX IF NOT EXISTS idx_inventory_transfer_transfer_code ON yapa.inventory_transfer (transfer_code);
CREATE INDEX IF NOT EXISTS idx_inventory_transfer_source_wh ON yapa.inventory_transfer (source_warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inventory_transfer_destination_wh ON yapa.inventory_transfer (destination_warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inventory_transfer_status ON yapa.inventory_transfer (status);
CREATE INDEX IF NOT EXISTS idx_inventory_transfer_requested_by ON yapa.inventory_transfer (requested_by_user_id);

-- Combined index for common query pattern
CREATE INDEX IF NOT EXISTS idx_inventory_transfer_wh_status ON yapa.inventory_transfer (source_warehouse_id, destination_warehouse_id, status);

-- Optional: Foreign keys to other tables
-- ALTER TABLE yapa.inventory_transfer ADD CONSTRAINT fk_transfer_source_warehouse FOREIGN KEY (source_warehouse_id) REFERENCES yapa.warehouses (id) ON DELETE RESTRICT;
-- ALTER TABLE yapa.inventory_transfer ADD CONSTRAINT fk_transfer_destination_warehouse FOREIGN KEY (destination_warehouse_id) REFERENCES yapa.warehouses (id) ON DELETE RESTRICT;
-- ALTER TABLE yapa.inventory_transfer ADD CONSTRAINT fk_transfer_requested_by_user FOREIGN KEY (requested_by_user_id) REFERENCES yapa.users (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.inventory_transfer ADD CONSTRAINT fk_transfer_approver_user FOREIGN KEY (approver_user_id) REFERENCES yapa.users (id) ON DELETE SET NULL;