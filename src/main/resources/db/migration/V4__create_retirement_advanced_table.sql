DROP TABLE IF EXISTS retirement_advanced;

CREATE TABLE retirement_advanced (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT NOT NULL REFERENCES users(id),
    plan_name VARCHAR(255),
    current_step INT DEFAULT 1,
    is_advanced BOOLEAN DEFAULT TRUE,
    advanced_data JSONB,
    target_fund NUMERIC(19,2),
    monthly_investment_required NUMERIC(19,2),
    final_life_expectancy INT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);
