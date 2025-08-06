-- V11__create_reservations_table.sql

CREATE SCHEMA IF NOT EXISTS yapa;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS yapa.reservations
(
    id                  UUID PRIMARY KEY    DEFAULT gen_random_uuid(),
    reservation_code    TEXT       NOT NULL UNIQUE,
    order_id            TEXT,
    reservation_type    TEXT       NOT NULL CHECK (reservation_type IN ('SALES_ORDER', 'WORK_ORDER', 'TRANSFER_ORDER')),
    requested_by_user_id TEXT,
    request_timestamp   TIMESTAMPTZ NOT NULL,
    expires_at          TIMESTAMPTZ,
    reserved_at         TIMESTAMPTZ,
    status              TEXT       NOT NULL CHECK (status IN ('PENDING', 'RESERVED', 'PARTIALLY_FULFILLED', 'FULFILLED', 'CANCELLED', 'EXPIRED')),
    customer_id         TEXT,
    warehouse_id        TEXT       NOT NULL,
    notes               TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

-- Basic indexes for common fields
CREATE INDEX IF NOT EXISTS idx_reservations_order_id ON yapa.reservations (order_id);
CREATE INDEX IF NOT EXISTS idx_reservations_warehouse_id ON yapa.reservations (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_reservations_status ON yapa.reservations (status);
CREATE INDEX IF NOT EXISTS idx_reservations_expires_at ON yapa.reservations (expires_at);

-- Combined index for common query pattern
CREATE INDEX IF NOT EXISTS idx_reservations_warehouse_status ON yapa.reservations (warehouse_id, status);

-- Optional: Foreign keys to other tables
-- ALTER TABLE yapa.reservations ADD CONSTRAINT fk_reservations_order FOREIGN KEY (order_id) REFERENCES yapa.orders (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.reservations ADD CONSTRAINT fk_reservations_user FOREIGN KEY (requested_by_user_id) REFERENCES yapa.users (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.reservations ADD CONSTRAINT fk_reservations_customer FOREIGN KEY (customer_id) REFERENCES yapa.customers (id) ON DELETE SET NULL;
-- ALTER TABLE yapa.reservations ADD CONSTRAINT fk_reservations_warehouse FOREIGN KEY (warehouse_id) REFERENCES yapa.warehouses (id) ON DELETE RESTRICT;