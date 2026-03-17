CREATE TABLE retirement_basic (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    current_age INT NOT NULL,
    retire_age INT NOT NULL,
    monthly_expense NUMERIC(19, 2) NOT NULL,
    inflation_rate DOUBLE PRECISION NOT NULL,
    life_expectancy INT NOT NULL,
    pre_retire_return DOUBLE PRECISION NOT NULL,
    post_retire_return DOUBLE PRECISION NOT NULL,
    total_funds_needed NUMERIC(19, 2),
    plan_name VARCHAR(255),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
