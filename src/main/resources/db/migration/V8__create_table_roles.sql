CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_users FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_roles FOREIGN KEY(role_id) REFERENCES roles(id)
);

INSERT INTO roles (name) VALUES('PARTICIPANT');
INSERT INTO roles (name) VALUES('MODERATOR');
INSERT INTO roles (name) VALUES('ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 3);
