CREATE TABLE IF NOT EXISTS net_worth_items (
    id BIGSERIAL PRIMARY KEY,
    snapshot_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    amount DECIMAL(19, 2),
    type VARCHAR(50) NOT NULL,
    CONSTRAINT fk_snapshot
        FOREIGN KEY(snapshot_id)
        REFERENCES net_worth_snapshots(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_net_worth_items_snapshot_id ON net_worth_items(snapshot_id);
CREATE INDEX idx_net_worth_items_type ON net_worth_items(type);
