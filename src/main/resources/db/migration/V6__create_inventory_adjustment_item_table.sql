-- V3__create_inventory_adjustment_item_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;

CREATE TABLE IF NOT EXISTS yapa.inventory_adjustment_item
(
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    adjustment_id          UUID NOT NULL,
    sku                    TEXT NOT NULL,
    variant_code           TEXT,
    quantity_change        INTEGER NOT NULL,
    old_quantity_on_hand   INTEGER,
    new_quantity_on_hand   INTEGER,
    serial_no              TEXT,
    item_notes             TEXT,
    delta_qty              INTEGER,
    unit_cost_minor        BIGINT,
    batch_no               TEXT,
    location_id            TEXT,
    created_at             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes for common foreign key and lookup fields
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_item_adjustment_id ON yapa.inventory_adjustment_item (adjustment_id);
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_item_sku ON yapa.inventory_adjustment_item (sku);
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_item_variant_code ON yapa.inventory_adjustment_item (variant_code);
CREATE INDEX IF NOT EXISTS idx_inventory_adjustment_item_serial_no ON yapa.inventory_adjustment_item (serial_no);

-- Foreign key to link items to their parent adjustment
ALTER TABLE yapa.inventory_adjustment_item
    ADD CONSTRAINT fk_adjustment_item_adjustment FOREIGN KEY (adjustment_id)
        REFERENCES yapa.inventory_adjustment (id) ON DELETE CASCADE;

-- Optional: If you have a separate table for locations, you may want to add a foreign key
-- ALTER TABLE yapa.inventory_adjustment_item ADD CONSTRAINT fk_inventory_adjustment_item_location FOREIGN KEY (location_id) REFERENCES yapa.locations (id) ON DELETE RESTRICT;

-- Optional: If you have a separate table for inventory variants, you may want to add a foreign key
-- ALTER TABLE yapa.inventory_adjustment_item ADD CONSTRAINT fk_inventory_adjustment_item_variant FOREIGN KEY (sku, variant_code) REFERENCES yapa.variants (sku, variant_code) ON DELETE RESTRICT;