-- V3: Fix schema for Google OAuth2, password reset, and role system

-- 1) Fix user_roles: add id column as primary key (entity uses @GeneratedValue id)
ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS user_roles_pkey;
ALTER TABLE user_roles ADD COLUMN IF NOT EXISTS id BIGSERIAL;
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'user_roles_pkey') THEN
        ALTER TABLE user_roles ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);
    END IF;
END $$;
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'uq_user_role') THEN
        ALTER TABLE user_roles ADD CONSTRAINT uq_user_role UNIQUE (user_id, role_id);
    END IF;
END $$;

-- 2) Make password nullable (Google users have no password)
ALTER TABLE users ALTER COLUMN password DROP NOT NULL;

-- 3) Add google_id column
ALTER TABLE users ADD COLUMN IF NOT EXISTS google_id VARCHAR(255);

-- 4) Add missing roles
INSERT INTO roles (name) VALUES ('ROLE_CEO')   ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_OWNER') ON CONFLICT DO NOTHING;

-- 5) Create password_reset_tokens table
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id        BIGSERIAL PRIMARY KEY,
    token     VARCHAR(255) NOT NULL UNIQUE,
    user_id   BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    used      BOOLEAN NOT NULL DEFAULT FALSE
);

-- 6) Fix user_profiles: add missing columns for UserProfile entity
ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS date_of_birth DATE;
ALTER TABLE user_profiles ADD COLUMN IF NOT EXISTS gender VARCHAR(50);
