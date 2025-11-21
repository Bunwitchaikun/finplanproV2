CREATE TABLE insurance_policies (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    policy_number VARCHAR(255) NOT NULL,
    life_coverage NUMERIC(19, 2) DEFAULT 0,
    disability_coverage NUMERIC(19, 2) DEFAULT 0,
    health_care_room NUMERIC(19, 2) DEFAULT 0,
    health_care_per_visit NUMERIC(19, 2) DEFAULT 0,
    health_care_opd NUMERIC(19, 2) DEFAULT 0,
    daily_compensation NUMERIC(19, 2) DEFAULT 0,
    critical_illness_coverage NUMERIC(19, 2) DEFAULT 0,
    main_premium NUMERIC(19, 2) DEFAULT 0,
    rider_premium NUMERIC(19, 2) DEFAULT 0,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE (user_id, policy_number)
);
