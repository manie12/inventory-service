-- V5__create_cycle_count_session_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.cycle_count_session
(
    id                 UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    cycle_session_code TEXT       NOT NULL UNIQUE,
    warehouse_id       TEXT       NOT NULL,
    location_id        TEXT,
    location_scope     TEXT,
    count_method       TEXT       NOT NULL CHECK (count_method IN ('CYCLE', 'FULL', 'SPOT')),
    cycle_status       TEXT       NOT NULL CHECK (cycle_status IN ('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'RECONCILED', 'CANCELLED')),
    count_type         TEXT       NOT NULL CHECK (count_type IN ('ADHOC', 'SCHEDULED', 'BLIND', 'VERIFICATION')),
    counted_by_user_id TEXT,
    approved_by_user_id TEXT,
    approval_date      TIMESTAMPTZ,
    scheduled_at       TIMESTAMPTZ,
    started_at         TIMESTAMPTZ,
    completed_at       TIMESTAMPTZ,
    adjustment_id      UUID,
    notes              TEXT,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by         TEXT       NOT NULL
);

-- Basic indexes for common fields
CREATE INDEX IF NOT EXISTS idx_cycle_count_session_warehouse_id ON yapa.cycle_count_session (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_cycle_count_session_status ON yapa.cycle_count_session (cycle_status);
CREATE INDEX IF NOT EXISTS idx_cycle_count_session_start_date ON yapa.cycle_count_session (started_at);
CREATE INDEX IF NOT EXISTS idx_cycle_count_session_location_id ON yapa.cycle_count_session (location_id);

-- Combined index for common query pattern
CREATE INDEX IF NOT EXISTS idx_cycle_count_session_warehouse_status ON yapa.cycle_count_session (warehouse_id, cycle_status);

-- Foreign key to link to a potential stock adjustment
ALTER TABLE yapa.cycle_count_session
    ADD CONSTRAINT fk_cycle_count_session_adjustment FOREIGN KEY (adjustment_id)
        REFERENCES yapa.inventory_adjustment (id) ON DELETE SET NULL;

-- Optional: Foreign keys to user and warehouse tables
-- ALTER TABLE yapa.cycle_count_session ADD CONSTRAINT fk_cycle_count_session_counted_by FOREIGN KEY (counted_by_user_id) REFERENCES yapa.users (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.cycle_count_session ADD CONSTRAINT fk_cycle_count_session_approved_by FOREIGN KEY (approved_by_user_id) REFERENCES yapa.users (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.cycle_count_session ADD CONSTRAINT fk_cycle_count_session_created_by FOREIGN KEY (created_by) REFERENCES yapa.users (id) ON DELETE RESTRICT;
-- ALTER TABLE yapa.cycle_count_session ADD CONSTRAINT fk_cycle_count_session_warehouse FOREIGN KEY (warehouse_id) REFERENCES yapa.warehouses (id) ON DELETE RESTRICT;