-- V4__create_cycle_count_line_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;

CREATE TABLE IF NOT EXISTS yapa.cycle_count_line
(
    id                         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cycle_session_id           UUID NOT NULL,
    sku                        TEXT NOT NULL,
    variant_code               TEXT,
    location_id                TEXT,
    system_quantity            INTEGER,
    counted_quantity           INTEGER,
    discrepancy                INTEGER,
    delta_qty                  INTEGER,
    count_result_variance_type TEXT CHECK (count_result_variance_type IN ('MATCH', 'SHORTAGE', 'OVERAGE')),
    count_status               TEXT CHECK (count_status IN ('PENDING', 'COUNTED', 'APPROVED', 'REJECTED')),
    adjustment_item_id         BIGINT,
    batch_no                   TEXT,
    serial_no                  TEXT,
    item_notes                 TEXT,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes for efficient lookups
CREATE INDEX IF NOT EXISTS idx_cycle_count_line_session_id ON yapa.cycle_count_line (cycle_session_id);
CREATE INDEX IF NOT EXISTS idx_cycle_count_line_sku ON yapa.cycle_count_line (sku);
CREATE INDEX IF NOT EXISTS idx_cycle_count_line_location_id ON yapa.cycle_count_line (location_id);
CREATE INDEX IF NOT EXISTS idx_cycle_count_line_status ON yapa.cycle_count_line (count_status);

-- Foreign key to link count lines to their parent session
-- This assumes a 'cycle_coun
-- t_session' table exists with an 'id' column.
-- ALTER TABLE yapa.cycle_count_line ADD CONSTRAINT fk_cycle_count_line_session FOREIGN KEY (cycle_session_id) REFERENCES yapa.cycle_count_session (id) ON DELETE CASCADE;

-- Optional: Foreign key for the adjustment item
-- ALTER TABLE yapa.cycle_count_line ADD CONSTRAINT fk_cycle_count_line_adjustment_item FOREIGN KEY (adjustment_item_id) REFERENCES yapa.inventory_adjustment_item (id) ON DELETE SET NULL;