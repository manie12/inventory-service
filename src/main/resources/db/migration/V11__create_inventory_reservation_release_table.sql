-- V8__create_inventory_reservation_release_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.inventory_reservation_release
(
    id                  UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    release_code        TEXT       NOT NULL UNIQUE,
    reservation_id      UUID       NOT NULL,
    reservation_code    TEXT,
    release_timestamp   TIMESTAMPTZ NOT NULL,
    order_id            TEXT,
    warehouse_id        TEXT       NOT NULL,
    release_type        TEXT       NOT NULL CHECK (release_type IN ('FULFILLED', 'CANCELLED', 'EXPIRED')),
    release_reason      TEXT,
    released_at         TIMESTAMPTZ NOT NULL,
    released_by         TEXT,
    notes               TEXT,
    shipment_id         TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by          TEXT       NOT NULL
    );

-- Basic indexes for common fields
CREATE INDEX IF NOT EXISTS idx_inv_res_release_reservation_id ON yapa.inventory_reservation_release (reservation_id);
CREATE INDEX IF NOT EXISTS idx_inv_res_release_warehouse_id ON yapa.inventory_reservation_release (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inv_res_release_order_id ON yapa.inventory_reservation_release (order_id);
CREATE INDEX IF NOT EXISTS idx_inv_res_release_shipment_id ON yapa.inventory_reservation_release (shipment_id);

-- Combined index for common query pattern
CREATE INDEX IF NOT EXISTS idx_inv_res_release_reservation_code ON yapa.inventory_reservation_release (reservation_code);

-- Foreign key to link this release to its parent reservation
-- ALTER TABLE yapa.inventory_reservation_release
--     ADD CONSTRAINT fk_inv_res_release_reservation FOREIGN KEY (reservation_id)
--         REFERENCES yapa.inventory_reservation (id) ON DELETE CASCADE;

-- Optional: Foreign key to an orders table
-- ALTER TABLE yapa.inventory_reservation_release
--     ADD CONSTRAINT fk_inv_res_release_order FOREIGN KEY (order_id)
--         REFERENCES yapa.orders (id) ON DELETE SET NULL;