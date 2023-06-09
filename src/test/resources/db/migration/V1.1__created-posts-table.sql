CREATE TABLE posts (
	id BIGSERIAL PRIMARY KEY,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	url VARCHAR(200) NOT NULL,
	caption VARCHAR(240),
	lat REAL CHECK(lat IS NULL OR (lng IS NOT NULL AND lat >= -90 AND lat <= 90)),
	lng REAL CHECK(lng IS NULL OR (lat IS NOT NULL AND lng >= -180 AND lng <= 180)),
	-- todo: create index for this reference
	user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE
);
