CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    short_biography VARCHAR(30),
    biography TEXT
);

INSERT INTO users(full_name, username, email, password) VALUES ('Master Ulysses', 'uly', 'umgs@gmail.com', '$2a$12$xNMAH.TMbU628vhY9vW5ku6JZAi7UcSamIVpz.MyuKA48nYAbnSbm');