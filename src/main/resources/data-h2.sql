-- =======================
-- Roles
-- =======================
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

-- =======================
-- Usuario admin inicial
-- =======================
INSERT INTO users (dni, password_hash)
VALUES ('11111111M', '$2b$12$bY4cLdYNUORkLLgHdjlvP.BsCyjAE7tSuToiaAQekXefykPWbze3O');

INSERT INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES ('Margarita', 'Vet', 'margarita@vetclinic.com', '600111111', '11111111M');

-- Asignar rol al usuario admin
INSERT INTO roles_users (user_id, role_id)
VALUES ('11111111M', 1);

-- =======================
-- Usuarios normales (ejemplo)
-- =======================
INSERT INTO users (dni, password_hash) VALUES ('22222222U', 'user123');
INSERT INTO users (dni, password_hash) VALUES ('33333333U', 'user456');

INSERT INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES ('Carlos', 'Pérez', 'carlos@vetclinic.com', '600222222', '22222222U');

INSERT INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES ('Ana', 'López', 'ana@vetclinic.com', '600333333', '33333333U');

-- Asignar rol USER a usuarios normales
INSERT INTO roles_users (user_id, role_id) VALUES ('22222222U', 2);
INSERT INTO roles_users (user_id, role_id) VALUES ('33333333U', 2);

-- =======================
-- Pacientes
-- =======================
INSERT INTO patients (name, age, breed, gender, user_id)
VALUES ('Firulais', 5, 'Labrador', 'Macho', '11111111M');

INSERT INTO patients (name, age, breed, gender, user_id)
VALUES ('Michi', 3, 'Siames', 'Hembra', '22222222U');

-- =======================
-- Citas
-- =======================
INSERT INTO appointments (date_time, type, reason, status, patient_id)
VALUES ('2025-09-25 10:00:00', 'STANDARD', 'Revisión general', 'PENDING', 1);

INSERT INTO appointments (date_time, type, reason, status, patient_id)
VALUES ('2025-09-26 13:30:00', 'EMERGENCY', 'Accidente doméstico', 'PENDING', 2);

-- =======================
-- Tratamientos
-- =======================
INSERT INTO treatments (description, treatment_date, patient_id)
VALUES ('Vacunación anual', '2025-09-25 11:00:00', 1);

INSERT INTO treatments (description, treatment_date, patient_id)
VALUES ('Limpieza dental', '2025-09-26 15:00:00', 2);

-- =======================
-- Citas de prueba para hoy
-- =======================
INSERT INTO appointments (date_time, type, reason, status, patient_id)
VALUES ('2025-09-30 09:30:00', 'STANDARD', 'Chequeo rápido', 'PENDING', 1);

INSERT INTO appointments (date_time, type, reason, status, patient_id)
VALUES ('2025-09-30 11:00:00', 'STANDARD', 'Vacuna anual', 'PENDING', 2);

INSERT INTO appointments (date_time, type, reason, status, patient_id)
VALUES ('2025-05-30 12:30:00', 'STANDARD', 'Dolor de estómago', 'PENDING', 1);
