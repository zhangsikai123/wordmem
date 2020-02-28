drop table if exists tests;
create table if not exists tests
(
    id          integer primary key autoincrement,
    f1          int,
    f2          varchar(100),
    f3          boolean,
    f4          datetime,
    create_time datetime,
    update_time datetime
);

drop table if exists words;
create table if not exists words
(
    id          integer primary key autoincrement,
    value       varchar(100),
    annotation  varchar(255),
    create_time datetime,
    update_time datetime

);

drop table if exists notes;
create table if not exists notes
(
    id          integer primary key autoincrement,
    word_id     integer,
    value       varchar(255),
    'index'     integer,
    create_time datetime,
    update_time datetime
)
