-- =======================
-- Roles
-- =======================
INSERT IGNORE INTO roles (name) VALUES ('ADMIN'), ('USER');

-- =======================
-- Usuario admin inicial
-- =======================
INSERT IGNORE INTO users (dni, password_hash)
VALUES ('11111111M', '$2b$12$bY4cLdYNUORkLLgHdjlvP.BsCyjAE7tSuToiaAQekXefykPWbze3O'); -- pas margarita123

INSERT IGNORE INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES ('Margarita', 'Vet', 'margarita@vetclinic.com', '600111111', '11111111M');

-- Asignar rol al usuario admin
INSERT IGNORE INTO roles_users (user_id, role_id)
VALUES ('11111111M', 1);

-- =======================
-- Usuarios normales (ejemplo)
-- =======================
INSERT INTO users (dni, password_hash)
VALUES 
('22222222U', 'user123'),
('33333333U', 'user456');

INSERT INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES
('Carlos', 'Pérez', 'carlos@vetclinic.com', '600222222', '22222222U'),
('Ana', 'López', 'ana@vetclinic.com', '600333333', '33333333U');

-- Asignar rol USER a usuarios normales
INSERT INTO roles_users (user_id, role_id)
VALUES 
('22222222U', 2),
('33333333U', 2);

-- =======================
-- Pacientes
-- =======================
INSERT INTO patients (name, age, breed, gender, user_id)
VALUES
('Firulais', 5, 'Labrador', 'Macho', '11111111M'),
('Michi', 3, 'Siames', 'Hembra', '22222222U');

-- =======================
-- Citas
-- =======================
INSERT INTO appointments (date_time, type, reason, status, patient_id)
VALUES
('2025-09-25 10:00:00', 'STANDARD', 'Revisión general', 'PENDING', 1),
('2025-09-26 13:30:00', 'EMERGENCY', 'Accidente doméstico', 'PENDING', 2);

-- =======================
-- Tratamientos
-- =======================
INSERT INTO treatments (description, treatment_date, patient_id)
VALUES
('Vacunación anual', '2025-09-25 11:00:00', 1),
('Limpieza dental', '2025-09-26 15:00:00', 2);
