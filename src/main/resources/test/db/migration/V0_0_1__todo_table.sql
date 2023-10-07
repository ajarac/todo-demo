DROP TABLE IF EXISTS todos;

CREATE TABLE todo
(
    id          uuid         NOT NULL DEFAULT random_uuid(),
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    done        boolean      NOT NULL,
    created  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "PK___payments___id" PRIMARY KEY (id)
)
