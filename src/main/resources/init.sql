-- Garantiza la existencia de la base y el usuario desde el arranque
CREATE DATABASE IF NOT EXISTS vetclinic;

CREATE USER IF NOT EXISTS 'myuser'@'%' IDENTIFIED BY 'secret';

GRANT ALL PRIVILEGES ON vetclinic.* TO 'myuser'@'%';

FLUSH PRIVILEGES;
