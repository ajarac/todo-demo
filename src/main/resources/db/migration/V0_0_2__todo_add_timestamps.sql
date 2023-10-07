ALTER TABLE todos
    ADD COLUMN created_at timestamp NOT NULL DEFAULT NOW(),
    ADD COLUMN updated_at timestamp NOT NULL DEFAULT NOW();

UPDATE todos SET created_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP;
