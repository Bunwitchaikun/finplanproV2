-- V2 Auth & User Profile Enhancements

-- 1) เพิ่มคอลัมน์ใหม่ใน user_profiles (ตาม entity version ล่าสุด)
ALTER TABLE user_profiles
ADD COLUMN IF NOT EXISTS full_name VARCHAR(255),
ADD COLUMN IF NOT EXISTS age INT,
ADD COLUMN IF NOT EXISTS phone VARCHAR(100),
ADD COLUMN IF NOT EXISTS email VARCHAR(255);

-- 2) ตรวจสอบว่า user_id มี unique constraint (OneToOne)
ALTER TABLE user_profiles
ADD CONSTRAINT IF NOT EXISTS uq_user_profile_user UNIQUE (user_id);

-- 3) ตรวจสอบ roles table
INSERT INTO roles (name)
VALUES ('ROLE_USER')
ON CONFLICT DO NOTHING;

INSERT INTO roles (name)
VALUES ('ROLE_ADMIN')
ON CONFLICT DO NOTHING;
