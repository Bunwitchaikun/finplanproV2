CREATE TABLE tax_records (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tax_year INT NOT NULL,
    annual_income NUMERIC(19, 2) DEFAULT 0,
    total_deduction NUMERIC(19, 2) DEFAULT 0,
    net_income NUMERIC(19, 2) DEFAULT 0,
    tax_payable NUMERIC(19, 2) DEFAULT 0,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
