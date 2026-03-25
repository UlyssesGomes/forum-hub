ALTER TABLE responses DROP COLUMN author;
ALTER TABLE responses ADD COLUMN author_id BIGINT NOT NULL;
ALTER TABLE responses ADD CONSTRAINT fk_author_responses FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE;