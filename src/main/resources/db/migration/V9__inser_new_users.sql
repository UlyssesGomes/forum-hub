ALTER TABLE users ADD COLUMN is_active BOOLEAN DEFAULT true;


INSERT INTO users(email, password, full_name, nickname, is_verified, is_active) VALUES ('joao@gmail.com', '$2y$10$/8EepTsy2reD5vKLPGn.gOXfFs5b6jjoaaWx/79kvdAfu0U0f4vmS', 'João', 'jao', 1, 1);
INSERT INTO users (email, password, full_name, nickname, is_verified, is_active) VALUES ('maria@gmail.com', '$2y$10$/8EepTsy2reD5vKLPGn.gOXfFs5b6jjoaaWx/79kvdAfu0U0f4vmS', 'Maria', 'maria', 1, 1);

INSERT INTO user_roles (user_id, role_id) VALUES(2, 1);
INSERT INTO user_roles (user_id, role_id) VALUES(3, 2);