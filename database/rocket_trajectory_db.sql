-- =============================================================================
-- Rocket Trajectory Simulator (RTS) — MySQL schema
-- Semester-2 Web-application Coursework
-- -----------------------------------------------------------------------------
-- Run this script ONCE in MySQL to (re)create the database, tables and seed
-- data.  All column names match exactly the names the Java services and
-- controllers expect, so no further configuration is required.
-- =============================================================================

DROP DATABASE IF EXISTS rocket_trajectory_db;
CREATE DATABASE rocket_trajectory_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE rocket_trajectory_db;

-- -----------------------------------------------------------------------------
-- TABLE: users
-- Two roles exist:
--     ADMIN     - manages catalogue, users and inquiries
--     ENGINEER  - browses catalogue and runs trajectory simulations
-- Passwords are stored as SHA-256 hex digests (encryption requirement).
-- -----------------------------------------------------------------------------
CREATE TABLE users (
    user_id          INT AUTO_INCREMENT PRIMARY KEY,
    full_name        VARCHAR(100)  NOT NULL,
    email            VARCHAR(120)  NOT NULL UNIQUE,
    phone            VARCHAR(20)   NOT NULL UNIQUE,
    password         VARCHAR(128)  NOT NULL,
    role             ENUM('ADMIN','ENGINEER') NOT NULL DEFAULT 'ENGINEER',
    organization     VARCHAR(120),
    country          VARCHAR(80),
    failed_attempts  INT          NOT NULL DEFAULT 0,
    account_locked   BOOLEAN      NOT NULL DEFAULT FALSE,
    lock_time        DATETIME     NULL,
    reset_token      VARCHAR(120) NULL,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- TABLE: rockets
-- Domain entity carried over from the Sem-1 desktop project; expanded for the
-- web platform with status, launch year, description and audit fields.
-- -----------------------------------------------------------------------------
CREATE TABLE rockets (
    rocket_id        INT AUTO_INCREMENT PRIMARY KEY,
    rocket_code      VARCHAR(30)   NOT NULL UNIQUE,
    rocket_name      VARCHAR(120)  NOT NULL,
    country          VARCHAR(80)   NOT NULL,
    manufacturer     VARCHAR(120),
    height_m         DECIMAL(8,2)  NOT NULL,
    diameter_m       DECIMAL(8,2)  NOT NULL,
    mass_kg          DECIMAL(14,2) NOT NULL,
    thrust_kn        DECIMAL(12,2) NOT NULL,
    stages           INT           NOT NULL DEFAULT 1,
    status           ENUM('ACTIVE','RETIRED','IN_DEVELOPMENT') NOT NULL DEFAULT 'ACTIVE',
    launch_year      INT           NOT NULL,
    description      TEXT,
    added_by         INT,
    created_at       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rocket_added_by
        FOREIGN KEY (added_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- -----------------------------------------------------------------------------
-- TABLE: simulations
-- Each row is one trajectory run an engineer performed against a rocket.
-- -----------------------------------------------------------------------------
CREATE TABLE simulations (
    simulation_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT           NOT NULL,
    rocket_id         INT           NOT NULL,
    launch_angle_deg  DECIMAL(5,2)  NOT NULL,
    burn_time_s       DECIMAL(8,2)  NOT NULL,
    payload_kg        DECIMAL(12,2) NOT NULL,
    max_altitude_km   DECIMAL(10,3),
    max_velocity_ms   DECIMAL(12,2),
    range_km          DECIMAL(10,3),
    flight_time_s     DECIMAL(10,2),
    status            ENUM('SUCCESS','FAILED') NOT NULL DEFAULT 'FAILED',
    notes             TEXT,
    created_at        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sim_user
        FOREIGN KEY (user_id)   REFERENCES users(user_id)   ON DELETE CASCADE,
    CONSTRAINT fk_sim_rocket
        FOREIGN KEY (rocket_id) REFERENCES rockets(rocket_id) ON DELETE CASCADE
);

-- -----------------------------------------------------------------------------
-- TABLE: inquiries
-- Submissions from the public Contact form; reviewed by admins.
-- -----------------------------------------------------------------------------
CREATE TABLE inquiries (
    inquiry_id   INT AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(100)  NOT NULL,
    email        VARCHAR(120)  NOT NULL,
    subject      VARCHAR(150)  NOT NULL,
    message      TEXT          NOT NULL,
    created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- SEED DATA
-- -----------------------------------------------------------------------------
-- Default credentials (verify against PasswordUtil.hash() — SHA-256 hex):
--   admin@rocketsim.com   / Admin@123
--   alex@rocketsim.com    / Engineer@1
--   priya@rocketsim.com   / Engineer@1
-- The SHA2() function below produces the same digest as PasswordUtil so the
-- seeded users can log in immediately after running this script.
-- =============================================================================

INSERT INTO users (full_name, email, phone, password, role, organization, country) VALUES
('System Administrator', 'admin@rocketsim.com',  '9800000001',
 SHA2('Admin@123', 256),
 'ADMIN',    'RocketSim Mission Control', 'Nepal'),
('Alex Carter',          'alex@rocketsim.com',   '9800000002',
 SHA2('Engineer@1', 256),
 'ENGINEER', 'Kathmandu Aerospace Lab',   'Nepal'),
('Priya Karki',          'priya@rocketsim.com',  '9800000003',
 SHA2('Engineer@1', 256),
 'ENGINEER', 'Himalayan Space Initiative','Nepal');

INSERT INTO rockets
(rocket_code, rocket_name, country, manufacturer,
 height_m, diameter_m, mass_kg, thrust_kn, stages,
 status, launch_year, description, added_by) VALUES
('FAL-9-B5',  'Falcon 9 Block 5',           'United States', 'SpaceX',
 70.00, 3.70,   549054.00,  7607.00, 2,
 'ACTIVE',         2018, 'Two-stage partially reusable orbital launch vehicle.', 1),
('SAT-V',     'Saturn V',                   'United States', 'NASA / Boeing',
 110.60, 10.10, 2970000.00, 35100.00, 3,
 'RETIRED',        1967, 'Three-stage super heavy-lift launch vehicle of the Apollo program.', 1),
('PSLV-XL',   'PSLV-XL',                    'India',         'ISRO',
 44.40, 2.80,   320000.00,  4800.00, 4,
 'ACTIVE',         2008, 'Polar Satellite Launch Vehicle Extended variant.',     1),
('LM-5',      'Long March 5',               'China',         'CASC',
 57.00, 5.00,   867000.00, 10573.00, 2,
 'ACTIVE',         2016, 'Chinese heavy-lift launch vehicle.',                    1),
('SOY-2',     'Soyuz-2',                    'Russia',        'Roscosmos',
 46.30, 2.95,   312000.00,  4148.00, 3,
 'ACTIVE',         2004, 'Three-stage rocket family used for crewed and cargo missions.', 1),
('STAR-V2',   'Starship V2',                'United States', 'SpaceX',
 121.00, 9.00, 5000000.00, 74400.00, 2,
 'IN_DEVELOPMENT', 2024, 'Fully reusable super heavy-lift rocket under active development.', 1);

INSERT INTO simulations
(user_id, rocket_id, launch_angle_deg, burn_time_s, payload_kg,
 max_altitude_km, max_velocity_ms, range_km, flight_time_s,
 status, notes) VALUES
(2, 1, 85.00, 162.00, 15000.00,
 412.500, 7800.00,  85.420,  540.00, 'SUCCESS', 'Nominal LEO insertion profile.'),
(2, 3, 88.20, 240.00,  1500.00,
 631.000, 7600.00,  42.810,  980.00, 'SUCCESS', 'Sun-synchronous orbit profile.');

INSERT INTO inquiries (full_name, email, subject, message) VALUES
('Demo Researcher', 'demo@example.com', 'Question about the simulator',
 'Hello, I would like to know how the trajectory engine handles atmospheric drag.');

-- =============================================================================
-- End of schema script
-- =============================================================================
