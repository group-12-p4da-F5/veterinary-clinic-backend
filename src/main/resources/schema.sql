-- =======================
-- Roles
-- =======================
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- =======================
-- Users y perfiles
-- =======================
CREATE TABLE users (
    dni VARCHAR(20) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    role_id BIGINT,
    CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    phone_number VARCHAR(20),
    dni VARCHAR(20),
    CONSTRAINT fk_profiles_users FOREIGN KEY (dni) REFERENCES users(dni)
);

-- =======================
-- Pacientes
-- =======================
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    breed VARCHAR(100),
    gender VARCHAR(20),
    user_id VARCHAR(20),
    CONSTRAINT fk_patients_users FOREIGN KEY (user_id) REFERENCES users(dni)
);

-- =======================
-- Citas
-- =======================
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    date_time DATETIME,
    type VARCHAR(50),
    reason VARCHAR(255),
    status VARCHAR(50),
    patient_id INT,
    CONSTRAINT fk_appointments_patients FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- =======================
-- Tratamientos
-- =======================
CREATE TABLE treatments (
    treatment_id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    treatment_date DATETIME,
    patient_id INT,
    CONSTRAINT fk_treatments_patients FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);
