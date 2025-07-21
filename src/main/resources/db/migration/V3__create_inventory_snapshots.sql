CREATE TABLE IF NOT EXISTS yapa.inventory_snapshots
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    sku TEXT NOT NULL,
    location TEXT NOT NULL,
    on_hand INT NOT NULL,
    reserved INT NOT NULL,
    captured_at TIMESTAMPTZ NOT NULL DEFAULT NOW
(
)
    );

-- Fast look-ups for BI and reconciliation dashboards
CREATE INDEX IF NOT EXISTS idx_snapshots_sku_loc_time
    ON yapa.inventory_snapshots (sku, location, captured_at DESC);