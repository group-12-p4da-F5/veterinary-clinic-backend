-- Roles iniciales
INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

-- Usuario admin inicial (contraseña en texto plano, SIN hash todavía)
INSERT INTO users (dni, password_hash, role_id)
VALUES ('00000000A', 'admin123', 1);

-- Perfil admin
INSERT INTO profiles (first_name, last_name, email, phone_number, dni)
VALUES ('Admin', 'System', 'admin@vetclinic.com', '600000000', '00000000A');
