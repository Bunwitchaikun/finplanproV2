-- Add new columns to insurance_policies table if they don't exist
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS policy_name VARCHAR(255);
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS accident_coverage NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS savings_return NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS pension NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS unit_linked_benefits NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS opd_per_visit NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS compensation_per_day NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS early_mid_critical_illness NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS severe_critical_illness NUMERIC(19, 2) DEFAULT 0;
ALTER TABLE insurance_policies ADD COLUMN IF NOT EXISTS partial_accident_compensation NUMERIC(19, 2) DEFAULT 0;

-- Ensure unique constraint is correct by dropping old ones and creating the new one if it doesn't exist
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'insurance_policies_policy_number_key') THEN
        ALTER TABLE insurance_policies DROP CONSTRAINT insurance_policies_policy_number_key;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'insurance_policies_user_id_policy_number_key') THEN
        ALTER TABLE insurance_policies ADD CONSTRAINT insurance_policies_user_id_policy_number_key UNIQUE (user_id, policy_number);
    END IF;
END;
$$;
