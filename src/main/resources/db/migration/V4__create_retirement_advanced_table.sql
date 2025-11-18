DROP TABLE IF EXISTS retirement_advanced;

CREATE TABLE retirement_advanced (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_name VARCHAR(255),
    date_of_birth DATE,
    gender VARCHAR(255),
    retire_age INT,
    life_expectancy INT,
    lifestyle VARCHAR(255),
    desired_monthly_expense NUMERIC(19, 2),
    special_expense NUMERIC(19, 2),
    current_assets NUMERIC(19, 2),
    rmf_ssf NUMERIC(19, 2),
    pension NUMERIC(19, 2),
    annuity NUMERIC(19, 2),
    total_funds_needed NUMERIC(19, 2),
    fund_gap NUMERIC(19, 2),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
