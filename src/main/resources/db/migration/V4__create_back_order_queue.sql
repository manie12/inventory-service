CREATE TABLE IF NOT EXISTS yapa.back_order_queue
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
    order_id UUID NOT NULL, -- original sales order
    sku TEXT NOT NULL,
    location TEXT NOT NULL,
    qty_needed INT NOT NULL,
    queued_at TIMESTAMPTZ NOT NULL DEFAULT NOW
(
)
    );

CREATE INDEX IF NOT EXISTS idx_backorder_match
    ON yapa.back_order_queue (sku, location, queued_at);