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

create table if not exists words
(
    id          integer primary key autoincrement,
    value       varchar(100),
    annotation  varchar(255),
    mark        integer,
    create_time datetime,
    update_time datetime

);

create table if not exists notes
(
    id          integer primary key autoincrement,
    word_id     integer,
    value       varchar(255),
    'index'     integer,
    create_time datetime,
    update_time datetime
);

create table if not exists marks
(
    id          integer primary key autoincrement,
    word_id     integer,
    create_time datetime,
    update_time datetime
);

create table if not exists meta
(
    id          integer primary key autoincrement,
    name        varchar(255) unique,
    value       varchar(255),
    create_time datetime,
    update_time datetime
);

insert into meta ('name', 'value') values ('cursor', '0')