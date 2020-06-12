create table account (
    phone integer primary key,
    name TEXT not null
);

create table hall (
    id serial primary key,
    row integer not null,
    seat integer not null,
    price integer not null,
    account_phone integer references account(phone) on delete set null,
    unique (row, seat)
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