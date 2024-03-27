CREATE TABLE PERSON
(
    ID          BIGSERIAL PRIMARY KEY,
    NAME TEXT NOT NULL,
    CONSTRAINT name_length CHECK (length(name) <= 512)
);