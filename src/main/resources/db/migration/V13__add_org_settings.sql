-- Roles for hierarchy
INSERT INTO roles (name) VALUES ('ROLE_OWNER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_CEO')   ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_USER')  ON CONFLICT (name) DO NOTHING;

-- Organisation secret codes (singleton row id=1)
CREATE TABLE IF NOT EXISTS org_settings (
    id          INT PRIMARY KEY DEFAULT 1,
    owner_code  VARCHAR(100) NOT NULL DEFAULT 'owner23082546',
    ceo_code    VARCHAR(100) NOT NULL DEFAULT 'ceo23082546',
    admin_code  VARCHAR(100) NOT NULL DEFAULT 'admin23082546',
    CONSTRAINT single_row CHECK (id = 1)
);

INSERT INTO org_settings (id, owner_code, ceo_code, admin_code)
VALUES (1, 'owner23082546', 'ceo23082546', 'admin23082546')
ON CONFLICT (id) DO NOTHING;
