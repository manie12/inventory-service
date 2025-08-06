-- V9__create_inventory_reservation_release_item_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.inventory_reservation_release_item
(
    id                         UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    release_id                 UUID       NOT NULL,
    reservation_item_id        BIGINT,
    sku                        TEXT       NOT NULL,
    variant_code               TEXT,
    quantity_released          INTEGER    NOT NULL,
    picked_from_location_id    TEXT,
    batch_no                   TEXT,
    shipment_id                TEXT,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by                 TEXT       NOT NULL
    );

-- Indexes for efficient lookups
CREATE INDEX IF NOT EXISTS idx_inv_res_release_item_release_id ON yapa.inventory_reservation_release_item (release_id);
CREATE INDEX IF NOT EXISTS idx_inv_res_release_item_reservation_item_id ON yapa.inventory_reservation_release_item (reservation_item_id);
CREATE INDEX IF NOT EXISTS idx_inv_res_release_item_sku ON yapa.inventory_reservation_release_item (sku);
CREATE INDEX IF NOT EXISTS idx_inv_res_release_item_shipment_id ON yapa.inventory_reservation_release_item (shipment_id);

-- Foreign key to link this item to its parent release event
ALTER TABLE yapa.inventory_reservation_release_item
    ADD CONSTRAINT fk_inv_res_release_item_release FOREIGN KEY (release_id)
        REFERENCES yapa.inventory_reservation_release (id) ON DELETE CASCADE;

-- Optional: Foreign key to a shipment table
-- ALTER TABLE yapa.inventory_reservation_release_item
--     ADD CONSTRAINT fk_inv_res_release_item_shipment FOREIGN KEY (shipment_id)
--         REFERENCES yapa.shipments (id) ON DELETE SET NULL;