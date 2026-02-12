CREATE TABLE IF NOT EXISTS doctors (
    npi_number VARCHAR(10) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    specialty VARCHAR(50) NOT NULL,
    practice_start_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    insurance_bin VARCHAR(255) NOT NULL,
    insurance_pcn VARCHAR(255) NOT NULL,
    insurance_member_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS bills (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    doctor_npi_number VARCHAR(10) NOT NULL,
    consultation_fee DOUBLE NOT NULL,
    discount_amount DOUBLE NOT NULL,
    discount_percentage DOUBLE NOT NULL,
    tax_amount DOUBLE NOT NULL,
    total_amount DOUBLE NOT NULL,
    co_pay_amount DOUBLE NOT NULL,
    insurance_payable_amount DOUBLE NOT NULL,
    tax_rate_percentage DOUBLE NOT NULL,
    co_pay_rate_percentage DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_npi_number) REFERENCES doctors(npi_number)
);

