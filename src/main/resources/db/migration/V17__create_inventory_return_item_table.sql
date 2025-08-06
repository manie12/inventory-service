-- V14__create_inventory_return_item_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.inventory_return_item
(
    id                         UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    return_id                  UUID       NOT NULL,
    sku                        TEXT       NOT NULL,
    variant_code               TEXT,
    returned_quantity          INTEGER    NOT NULL,
    return_condition           TEXT       NOT NULL CHECK (return_condition IN ('UNOPENED', 'OPENED', 'USED', 'DAMAGED')),
    return_disposition         TEXT       NOT NULL CHECK (return_disposition IN ('RESTOCK', 'REFURBISH', 'SCRAP')),
    return_reason              TEXT       NOT NULL CHECK (return_reason IN ('DEFECTIVE', 'WRONG_ITEM_SENT', 'CHANGED_MIND', 'DAMAGED_IN_TRANSIT')),
    unit_cost_minor            BIGINT,
    location_id                TEXT,
    inspection_status          TEXT,
    serial_no                  TEXT,
    refund_amount_minor        BIGINT,
    restocked_location_id      TEXT,
    batch_no                   TEXT,
    expiry_date                DATE,
    item_notes                 TEXT,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- Indexes for efficient lookups
CREATE INDEX IF NOT EXISTS idx_inventory_return_item_return_id ON yapa.inventory_return_item (return_id);
CREATE INDEX IF NOT EXISTS idx_inventory_return_item_sku ON yapa.inventory_return_item (sku);
CREATE INDEX IF NOT EXISTS idx_inventory_return_item_condition ON yapa.inventory_return_item (return_condition);
CREATE INDEX IF NOT EXISTS idx_inventory_return_item_disposition ON yapa.inventory_return_item (return_disposition);

-- Foreign key to link items to the main return
ALTER TABLE yapa.inventory_return_item
    ADD CONSTRAINT fk_inventory_return_item_return FOREIGN KEY (return_id)
        REFERENCES yapa.inventory_return (id) ON DELETE CASCADE;

-- Optional: Foreign key to a locations table
-- ALTER TABLE yapa.inventory_return_item ADD CONSTRAINT fk_inventory_return_item_location FOREIGN KEY (location_id) REFERENCES yapa.locations (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.inventory_return_item ADD CONSTRAINT fk_inventory_return_item_restock_loc FOREIGN KEY (restocked_location_id) REFERENCES yapa.locations (id) ON DELETE SET NULL;