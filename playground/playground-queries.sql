create schema samplesjpaid;
set search_path to samplesjpaid,public;

select now();
select clock_timestamp();

select * from account;

insert into ITEM (ID, NAME, DESCRIPTION)
    select id, 'name' || id, 'stub description for ' || id from generate_series(1, 1000000) as id;

SELECT * FROM ITEM FETCH FIRST 3 ROWS ONLY OFFSET 10;

SELECT * FROM ITEM TABLESAMPLE bernoulli(10) FETCH FIRST 3 ROWS ONLY OFFSET 10;

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
COPY item FROM '/usr/share/import/items.txt'; --1,000,000 rows affected in 8 s 693 ms

select count(*) from item;

select * from item;

---

CREATE INDEX idx_id_gist ON item USING gist(id);
drop index idx_id_gist;

explain analyze
SELECT *
FROM item
ORDER BY id <-> 15648
LIMIT 6;


--- locking
CREATE TABLE ACCOUNT (
    id bigserial primary key,
    amount bigint not null default 1000
);

select * from account;

select id, sum(amount) from account group by rollup (1);
update account set amount = 1000 where 1=1;

SELECT pg_advisory_lock(1);
SELECT pg_advisory_xact_lock(1);
select pg_advisory_unlock(1);

select pg_backend_pid();

SELECT * FROM pg_locks WHERE pid = pg_backend_pid() AND locktype = 'advisory';

SELECT * FROM pg_locks WHERE  locktype = 'advisory';




insert into account (amount) values (1000), (1000), (1000);

ROLLBACK;

BEGIN;
    update samplesjpaid.account set amount = amount - 1 where id = 1;
    update samplesjpaid.account set amount = amount + 1 where id = 2;
COMMIT;

BEGIN;
    update samplesjpaid.account set amount = amount - 1 where id = 2;
    update samplesjpaid.account set amount = amount + 1 where id = 3;
COMMIT;

BEGIN;
    update samplesjpaid.account set amount = amount - 1 where id = 3;
    update samplesjpaid.account set amount = amount + 1 where id = 1;
COMMIT;

select * from samplesjpaid.account where id in (1,2,3) FOR NO KEY UPDATE;

select * from samplesjpaid.account;



--pgbench -r -U postgres -t 10000 -f /usr/share/import/bench-0.sql -c 8 -j 2 playground
--pgbench -r -U postgres -t 10000 -f /usr/share/import/bench-2.sql -c 8 -j 2 playground

--PGOPTIONS='-c default_transaction_isolation=serializable' pgbench -r -U postgres -t 1000 -f /usr/share/import/bench-1.sql -c 8 -j 2 playground
--PGOPTIONS='-c default_transaction_isolation=repeatable\ read' pgbench -r -U postgres -t 1000 -f /usr/share/import/bench-1.sql -c 8 -j 2 playground

--PGOPTIONS='-c default_transaction_isolation=repeatable\ read' pgbench -r -U postgres -t 1000 -f /usr/share/import/bench-1.sql -c 8 -j 2 playground

--PGOPTIONS='-c default_transaction_isolation=repeatable\ read' pgbench -r -U postgres -t 1000 -f /usr/share/import/bench-4.sql -c 8 -j 1 playground
--PGOPTIONS='-c default_transaction_isolation=read\ committed' pgbench -r -U postgres -t 1000 -f /usr/share/import/bench-4.sql -c 8 -j 2 playground

--PGOPTIONS='-c default_transaction_isolation=read\ committed' pgbench -r -U postgres -t 1000 -f /usr/share/import/bench-6.sql -c 8 -j 1 playground
--PGOPTIONS='-c default_transaction_isolation=repeatable\ read' pgbench -r -U postgres -t 1000 -f /usr/share/import/bench-6.sql -c 8 -j 1 playground
--PGOPTIONS='-c default_transaction_isolation=repeatable\ read' pgbench -r -U postgres -t 10 -f /usr/share/import/bench-6.sql -c 2 -j 1 playground --verbose-errors

