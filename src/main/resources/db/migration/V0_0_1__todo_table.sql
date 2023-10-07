DROP TABLE IF EXISTS todos;

CREATE TABLE todo
(
    id          uuid         NOT NULL DEFAULT uuid_generate_v4(),
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    done        boolean      NOT NULL,
    CONSTRAINT "PK___payments___id" PRIMARY KEY (id)
)
