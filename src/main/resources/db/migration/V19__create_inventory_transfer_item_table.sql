-- V16__create_inventory_transfer_item_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.inventory_transfer_item
(
    id                         UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    inventory_transfer_id      UUID       NOT NULL,
    sku                        TEXT       NOT NULL,
    variant_code               TEXT,
    quantity                   INTEGER    NOT NULL,
    batch_no                   TEXT,
    source_location_id         TEXT,
    destination_location_id    TEXT,
    serial_no                  TEXT,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- Indexes for efficient lookups and foreign keys
CREATE INDEX IF NOT EXISTS idx_inv_transfer_item_transfer_id ON yapa.inventory_transfer_item (inventory_transfer_id);
CREATE INDEX IF NOT EXISTS idx_inv_transfer_item_sku ON yapa.inventory_transfer_item (sku);
CREATE INDEX IF NOT EXISTS idx_inv_transfer_item_source_location ON yapa.inventory_transfer_item (source_location_id);
CREATE INDEX IF NOT EXISTS idx_inv_transfer_item_destination_location ON yapa.inventory_transfer_item (destination_location_id);

-- Foreign key to link items to the main transfer
ALTER TABLE yapa.inventory_transfer_item
    ADD CONSTRAINT fk_inv_transfer_item_transfer FOREIGN KEY (inventory_transfer_id)
        REFERENCES yapa.inventory_transfer (id) ON DELETE CASCADE;

-- Optional: Foreign key to a locations table
-- ALTER TABLE yapa.inventory_transfer_item ADD CONSTRAINT fk_transfer_item_source_loc FOREIGN KEY (source_location_id) REFERENCES yapa.locations (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.inventory_transfer_item ADD CONSTRAINT fk_transfer_item_dest_loc FOREIGN KEY (destination_location_id) REFERENCES yapa.locations (id) ON DELETE SET NULL;