-- V13__create_inventory_return_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.inventory_return
(
    id                      UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    return_code             TEXT       NOT NULL UNIQUE,
    original_order_id       TEXT,
    original_shipment_id    TEXT,
    purchase_order_id       TEXT,
    rma_number              TEXT,
    warehouse_id            TEXT       NOT NULL,
    return_source           TEXT       NOT NULL CHECK (return_source IN ('CUSTOMER', 'SUPPLIER')),
    return_status           TEXT       NOT NULL CHECK (return_status IN ('DRAFT', 'RECEIVED', 'INSPECTED', 'COMPLETED', 'CANCELLED')),
    received_at             TIMESTAMPTZ,
    inspected_at            TIMESTAMPTZ,
    returned_by_user_id     TEXT,
    external_reference_id   TEXT,
    completed_at            TIMESTAMPTZ,
    note                    TEXT,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by              TEXT       NOT NULL
    );

-- Indexes for common lookups and foreign keys
CREATE INDEX IF NOT EXISTS idx_inventory_return_return_code ON yapa.inventory_return (return_code);
CREATE INDEX IF NOT EXISTS idx_inventory_return_warehouse_id ON yapa.inventory_return (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inventory_return_status ON yapa.inventory_return (return_status);
CREATE INDEX IF NOT EXISTS idx_inventory_return_original_order_id ON yapa.inventory_return (original_order_id);
CREATE INDEX IF NOT EXISTS idx_inventory_return_original_shipment_id ON yapa.inventory_return (original_shipment_id);
CREATE INDEX IF NOT EXISTS idx_inventory_return_purchase_order_id ON yapa.inventory_return (purchase_order_id);

-- Combined index for common query pattern
CREATE INDEX IF NOT EXISTS idx_inventory_return_warehouse_status ON yapa.inventory_return (warehouse_id, return_status);

-- Optional: Foreign keys to other tables
-- ALTER TABLE yapa.inventory_return ADD CONSTRAINT fk_inventory_return_warehouse FOREIGN KEY (warehouse_id) REFERENCES yapa.warehouses (id) ON DELETE RESTRICT;
-- ALTER TABLE yapa.inventory_return ADD CONSTRAINT fk_inventory_return_user FOREIGN KEY (returned_by_user_id) REFERENCES yapa.users (id) ON DELETE SET NULL;