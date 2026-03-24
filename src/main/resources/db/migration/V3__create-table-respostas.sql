CREATE TABLE responses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    is_solved BOOLEAN DEFAULT FALSE,
    topic_id BIGINT,
    CONSTRAINT fk_topic FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE
);