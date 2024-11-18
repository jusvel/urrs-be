CREATE TABLE roles
(
    ID        INT     NOT NULL,
    role_name VARCHAR NOT NULL
);
ALTER TABLE roles ADD CONSTRAINT unique_roles_key UNIQUE (ID);
ALTER TABLE users
    ADD COLUMN role INT REFERENCES roles ("id") ON DELETE CASCADE;

INSERT INTO roles (ID, role_name) VALUES (0, 'USER'), (1, 'ORGANIZER'), (2, 'ADMIN');