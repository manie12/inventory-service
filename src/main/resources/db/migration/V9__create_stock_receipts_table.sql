-- V6__create_stock_receipts_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.stock_receipts
(
    id                      UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    receipt_id              TEXT       NOT NULL UNIQUE,
    warehouse_id            TEXT       NOT NULL,
    supplier_id             TEXT       NOT NULL,
    asn_id                  TEXT,
    received_by_user_id     TEXT,
    receipt_timestamp       TIMESTAMPTZ NOT NULL,
    status                  TEXT       NOT NULL CHECK (status IN ('PENDING', 'RECEIVED', 'CANCELLED')),
    external_reference_id   TEXT,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Basic indexes for common fields
CREATE INDEX IF NOT EXISTS idx_stock_receipts_warehouse_id ON yapa.stock_receipts (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_stock_receipts_supplier_id ON yapa.stock_receipts (supplier_id);
CREATE INDEX IF NOT EXISTS idx_stock_receipts_status ON yapa.stock_receipts (status);
CREATE INDEX IF NOT EXISTS idx_stock_receipts_received_by_user_id ON yapa.stock_receipts (received_by_user_id);
CREATE INDEX IF NOT EXISTS idx_stock_receipts_asn_id ON yapa.stock_receipts (asn_id);

-- Combined index for common query pattern
CREATE INDEX IF NOT EXISTS idx_stock_receipts_warehouse_status ON yapa.stock_receipts (warehouse_id, status);

-- Optional: Foreign keys to other tables
-- ALTER TABLE yapa.stock_receipts ADD CONSTRAINT fk_stock_receipts_warehouse FOREIGN KEY (warehouse_id) REFERENCES yapa.warehouses (id) ON DELETE RESTRICT;
-- ALTER TABLE yapa.stock_receipts ADD CONSTRAINT fk_stock_receipts_supplier FOREIGN KEY (supplier_id) REFERENCES yapa.suppliers (id) ON DELETE RESTRICT;
-- ALTER TABLE yapa.stock_receipts ADD CONSTRAINT fk_stock_receipts_user FOREIGN KEY (received_by_user_id) REFERENCES yapa.users (id) ON DELETE SET NULL;