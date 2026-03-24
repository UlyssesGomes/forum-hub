CREATE TABLE topics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    category ENUM('DEVELOPMENT', 'IA', 'FRONTEND', 'DATA', 'INNOVATION', 'MARKETING', 'DESIGN') NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    status ENUM('UNANSWERED', 'ANSWERED', 'SOLVED') NOT NULL,
    is_open BOOLEAN NOT NULL,
    quantity_responses INT NOT NULL,
    course_id BIGINT,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL
);