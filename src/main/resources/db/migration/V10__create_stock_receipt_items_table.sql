-- V7__create_stock_receipt_items_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.stock_receipt_items
(
    id                 UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    receipt_id         UUID       NOT NULL,
    sku                TEXT       NOT NULL,
    variant_code       TEXT,
    quantity_received  INTEGER    NOT NULL,
    purchase_order_id  TEXT,
    asn_id             TEXT,
    supplier_id        TEXT,
    unit_cost          NUMERIC(19, 4),
    target_location_id TEXT,
    inspection_result  TEXT       NOT NULL CHECK (inspection_result IN ('ACCEPTED', 'REJECTED', 'QUARANTINED')),
    batch_number       TEXT,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- Indexes for efficient lookups and foreign keys
CREATE INDEX IF NOT EXISTS idx_stock_receipt_items_receipt_id ON yapa.stock_receipt_items (receipt_id);
CREATE INDEX IF NOT EXISTS idx_stock_receipt_items_sku ON yapa.stock_receipt_items (sku);
CREATE INDEX IF NOT EXISTS idx_stock_receipt_items_purchase_order_id ON yapa.stock_receipt_items (purchase_order_id);
CREATE INDEX IF NOT EXISTS idx_stock_receipt_items_asn_id ON yapa.stock_receipt_items (asn_id);
CREATE INDEX IF NOT EXISTS idx_stock_receipt_items_supplier_id ON yapa.stock_receipt_items (supplier_id);
CREATE INDEX IF NOT EXISTS idx_stock_receipt_items_target_location_id ON yapa.stock_receipt_items (target_location_id);

-- Foreign key constraint to link items to the main stock receipt
ALTER TABLE yapa.stock_receipt_items
    ADD CONSTRAINT fk_stock_receipt_items_receipt FOREIGN KEY (receipt_id)
        REFERENCES yapa.stock_receipts (id) ON DELETE CASCADE;

-- Optional: Foreign key to a purchase order table
-- ALTER TABLE yapa.stock_receipt_items ADD CONSTRAINT fk_stock_receipt_items_po FOREIGN KEY (purchase_order_id) REFERENCES yapa.purchase_orders (id) ON DELETE SET NULL;