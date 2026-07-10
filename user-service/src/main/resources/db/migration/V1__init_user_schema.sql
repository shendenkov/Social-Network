CREATE TABLE profiles
(
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    username VARCHAR(50) UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    avatar_url VARCHAR(500),
    bio VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE processed_events
(
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMPTZ NOT NULL
);