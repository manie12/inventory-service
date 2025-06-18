CREATE
EXTENSION IF NOT EXISTS pgcrypto;
CREATE SCHEMA IF NOT EXISTS yapa;

CREATE TABLE IF NOT EXISTS yapa.stock_items
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    tenant_id UUID NOT NULL,
    sku TEXT NOT NULL,
    location TEXT NOT NULL,
    on_hand INT NOT NULL,
    reserved INT NOT NULL DEFAULT 0,
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW
(
),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW
(
),
    UNIQUE
(
    tenant_id,
    sku,
    location
)
    );