create schema samplesjpaid;
set search_path to samplesjpaid,public;

insert into ITEM (ID, NAME, DESCRIPTION)
    select id, 'name' || id, 'stub description for ' || id from generate_series(1, 1048576) as id;

insert into ITEM (ID, DESCRIPTION)
select id, 'stub description for 1' from generate_series(1, 1000000) as id;

select nextval('item_id_seq') as id from generate_series(1, 1000000);

create extension pg_stat_statements with schema public;

select now();

show timezone ;

select * from pg_stat_statements where query ilike '%ITEM%';

select dbid, count(queryid) from pg_stat_statements group by dbid;

DROP TABLE ITEM;

CREATE TABLE ITEM
(
    ID          BIGSERIAL PRIMARY KEY,
    NAME TEXT NOT NULL,
    DESCRIPTION TEXT,
    LAST_UPDATED TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX ITEM_NAME_IDX ON ITEM (NAME);
ALTER SEQUENCE ITEM_ID_SEQ INCREMENT BY 100;

select nextval('ITEM_ID_SEQ');

select * from ITEM order by id;

select last_updated, count(id) from ITEM group by 1;

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