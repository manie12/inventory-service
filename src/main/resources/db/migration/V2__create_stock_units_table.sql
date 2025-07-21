CREATE TABLE IF NOT EXISTS yapa.stock_units
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
    serial_no TEXT,
    lot_no TEXT,
    expiry_date DATE,
    state TEXT NOT NULL DEFAULT 'ON_HAND'
    CHECK
(
    state
    IN
(
    'ON_HAND',
    'RESERVED',
    'ALLOCATED',
    'DAMAGED',
    'DISPOSED'
)),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now
(
),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now
(
),
    UNIQUE
(
    tenant_id,
    serial_no
),
    INDEX idx_stock_units_lookup
(
    tenant_id,
    sku,
    location,
    state
)
    );