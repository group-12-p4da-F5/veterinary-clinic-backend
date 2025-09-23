-- =======================
-- Roles
-- =======================
INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

-- =======================
-- Usuario admin inicial
-- =======================
INSERT INTO users (dni, password_hash, role_id)
VALUES ('11111111M', 'margarita123', 1);

-- =======================
-- Perfil admin
-- =======================
INSERT INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES ('Margarita', 'Vet', 'margarita@vetclinic.com', '600111111', '11111111M');

-- =======================
-- Usuarios normales (ejemplo)
-- =======================
INSERT INTO users (dni, password_hash, role_id)
VALUES 
('22222222U', 'user123', 2),
('33333333U', 'user456', 2);

INSERT INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES
('Carlos', 'Pérez', 'carlos@vetclinic.com', '600222222', '22222222U'),
('Ana', 'López', 'ana@vetclinic.com', '600333333', '33333333U');

-- =======================
-- Pacientes
-- =======================
INSERT INTO patients 
(name, age, breed, gender, identification_number, owner_first_name, owner_last_name, owner_dni, owner_phone, user_id)
VALUES
('Firulais', 5, 'Labrador', 'Macho', 'ID001', 'Laura', 'García', '12345678A', '600444444', '11111111M'),
('Michi', 3, 'Siames', 'Hembra', 'ID002', 'Pedro', 'Martínez', '87654321B', '600555555', '22222222U');

-- =======================
-- Appointments
-- =======================
INSERT INTO appointments 
(date_time, type, reason, status, patient_id)
VALUES
('2025-09-25 10:00:00', 'STANDARD', 'Revisión general', 'PENDING', 1),
('2025-09-26 14:30:00', 'EMERGENCY', 'Accidente doméstico', 'PENDING', 2);

-- =======================
-- Treatments
-- =======================
INSERT INTO treatments 
(description, treatment_date, patient_id)
VALUES
('Vacunación anual', '2025-09-25 11:00:00', 1),
('Limpieza dental', '2025-09-26 15:00:00', 2);
