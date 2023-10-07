ALTER TABLE todo
    ADD COLUMN created timestamp NOT NULL DEFAULT NOW(),
    ADD COLUMN updated timestamp NOT NULL DEFAULT NOW();

UPDATE todos SET created = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP;
