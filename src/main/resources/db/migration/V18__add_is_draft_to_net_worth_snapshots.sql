ALTER TABLE net_worth_snapshots ADD COLUMN IF NOT EXISTS is_draft BOOLEAN DEFAULT FALSE NOT NULL;
CREATE INDEX IF NOT EXISTS idx_net_worth_snapshots_is_draft ON net_worth_snapshots(user_id, is_draft);
