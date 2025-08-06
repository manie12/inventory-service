-- V12__create_reservation_items_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.reservation_items
(
    id                     UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    reservation_id         UUID       NOT NULL,
    sku                    TEXT       NOT NULL,
    variant_code           TEXT,
    quantity               INTEGER    NOT NULL,
    preferred_location_id  TEXT,
    batch_no               TEXT,
    created_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- Indexes for efficient lookups and foreign keys
CREATE INDEX IF NOT EXISTS idx_reservation_items_reservation_id ON yapa.reservation_items (reservation_id);
CREATE INDEX IF NOT EXISTS idx_reservation_items_sku ON yapa.reservation_items (sku);
CREATE INDEX IF NOT EXISTS idx_reservation_items_variant_code ON yapa.reservation_items (variant_code);
CREATE INDEX IF NOT EXISTS idx_reservation_items_preferred_location_id ON yapa.reservation_items (preferred_location_id);

-- Foreign key constraint to link items to the main reservation
ALTER TABLE yapa.reservation_items
    ADD CONSTRAINT fk_reservation_items_reservation FOREIGN KEY (reservation_id)
        REFERENCES yapa.reservations (id) ON DELETE CASCADE;

-- Optional: Foreign key to a locations table
-- ALTER TABLE yapa.reservation_items ADD CONSTRAINT fk_reservation_items_location FOREIGN KEY (preferred_location_id) REFERENCES yapa.locations (id) ON DELETE SET NULL;