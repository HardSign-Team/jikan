alter table if exists activities drop constraint if exists FKq6cjukylkgxdjkm9npk9va2f2;
alter table if exists timestamps drop constraint if exists FKr7l61wfcy0a9g712qjad40h5w;
drop table if exists activities cascade;
drop table if exists timestamps cascade;
drop table if exists users cascade;
