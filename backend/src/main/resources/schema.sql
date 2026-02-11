CREATE TABLE IF NOT EXISTS doctors (
    npi_number VARCHAR(10) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    specialty VARCHAR(50) NOT NULL,
    practice_start_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    insurance_bin VARCHAR(255) NOT NULL,
    insurance_pcn VARCHAR(255) NOT NULL,
    insurance_member_id VARCHAR(255) NOT NULL
);