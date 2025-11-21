CREATE TABLE net_worth_snapshots (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    snapshot_name VARCHAR(255),
    snapshot_date DATE NOT NULL,
    cash_and_equivalents NUMERIC(19, 2) DEFAULT 0,
    stocks NUMERIC(19, 2) DEFAULT 0,
    funds NUMERIC(19, 2) DEFAULT 0,
    real_estate NUMERIC(19, 2) DEFAULT 0,
    other_assets NUMERIC(19, 2) DEFAULT 0,
    credit_card_debt NUMERIC(19, 2) DEFAULT 0,
    home_loan NUMERIC(19, 2) DEFAULT 0,
    car_loan NUMERIC(19, 2) DEFAULT 0,
    other_liabilities NUMERIC(19, 2) DEFAULT 0,
    total_assets NUMERIC(19, 2) DEFAULT 0,
    total_liabilities NUMERIC(19, 2) DEFAULT 0,
    net_worth NUMERIC(19, 2) DEFAULT 0,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
