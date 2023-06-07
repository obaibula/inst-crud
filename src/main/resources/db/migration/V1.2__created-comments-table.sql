CREATE TABLE comments (
	id BIGSERIAL PRIMARY KEY,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	contents VARCHAR(240) NOT NULL,
	-- todo: create index for this reference
	user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	-- todo: create index for this reference
	post_id INTEGER NOT NULL REFERENCES posts(id) ON DELETE CASCADE
);
