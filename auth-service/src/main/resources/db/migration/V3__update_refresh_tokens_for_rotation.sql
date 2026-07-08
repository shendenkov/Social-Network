ALTER TABLE refresh_tokens
    RENAME COLUMN token TO token_hash;

ALTER TABLE refresh_tokens
    ALTER COLUMN token_hash TYPE VARCHAR(64);

ALTER TABLE refresh_tokens
    ADD COLUMN used_at TIMESTAMPTZ;

CREATE UNIQUE INDEX idx_refresh_tokens_token_hash
    ON refresh_tokens(token_hash);