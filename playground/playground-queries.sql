insert into ITEM (ID, NAME, DESCRIPTION)
    select id, 'name' || id, 'stub description for ' || id from generate_series(1, 1048576) as id;

insert into ITEM (ID, DESCRIPTION)
select id, 'stub description for 1' from generate_series(1, 1000000) as id;

create extension pg_stat_statements with schema public;

select * from pg_stat_statements where query ilike '%ITEM%';

DROP TABLE ITEM;

CREATE TABLE ITEM
(
    ID          BIGSERIAL PRIMARY KEY,
    NAME TEXT NOT NULL,
    DESCRIPTION TEXT
);
CREATE INDEX ITEM_NAME_IDX ON ITEM (NAME);


SELECT pg_stat_statements_reset();

truncate table item;
delete from item where 1=1;

SELECT pg_size_pretty(pg_relation_size('item'));

COPY item FROM '/usr/share/import/items.txt';

select count(*) from item;

select * from item;

CREATE TABLE ITEM
(
    ID          BIGINT PRIMARY KEY,
    DESCRIPTION TEXT NOT NULL
);

CREATE TABLE ITEM_UUID_AUTO
(
    ID          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    DESCRIPTION TEXT NOT NULL
);

CREATE TABLE ITEM_UUID
(
    ID          UUID PRIMARY KEY,
    DESCRIPTION TEXT NOT NULL
);

CREATE TABLE ITEM_NUMERIC_AUTO
(
    ID          BIGSERIAL PRIMARY KEY,
    DESCRIPTION TEXT NOT NULL
);

CREATE TABLE ITEM_NUMERIC
(
    ID          BIGSERIAL PRIMARY KEY,
    DESCRIPTION TEXT NOT NULL
);

ALTER SEQUENCE ITEM_NUMERIC_ID_SEQ INCREMENT BY 100 RESTART 100;