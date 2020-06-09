create table hall (
    id serial primary key ,
    row integer,
    seat integer,
    price integer,
    account_phone integer
);

create table account (
    phone integer primary key,
    name TEXT
);

insert into hall(row, seat, price) values (1, 1, 300);
insert into hall(row, seat, price) values (1, 2, 300);
insert into hall(row, seat, price) values (1, 3, 300);
insert into hall(row, seat, price) values (2, 1, 300);
insert into hall(row, seat, price) values (2, 2, 300);
insert into hall(row, seat, price) values (2, 3, 300);
insert into hall(row, seat, price) values (3, 1, 300);
insert into hall(row, seat, price) values (3, 2, 300);
insert into hall(row, seat, price) values (3, 3, 300);