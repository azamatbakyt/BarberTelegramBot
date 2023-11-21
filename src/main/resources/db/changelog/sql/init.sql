drop table if exists customer_service_group, customer_service, clients cascade;

create table if not exists customer_service_group
(
    id
    bigserial
    primary
    key,
    name
    varchar
(
    255
) not null unique
    );


create table if not exists customer_service
(
    id bigserial primary key,
    duration varchar (255) not null,
    name varchar(255) not null,
    price varchar(255) not null,
    customer_service_group_id bigint not null
    constraint fk_customer_service_customer_service_group
    references customer_service_group
    );



create table if not exists clients
(
    id bigserial primary key,
    chat_id bigserial unique,
    name varchar(255),
    phone varchar(255) unique,
    registration_completed boolean not null
);

create table if not exists timeslots(
    id  bigserial primary key,
    start_time time,
    end_time time
);

create table if not exists schedules(
    id bigserial primary key,
    day_of_week varchar(255) ,
    timeslot_id bigint not null references timeslots(id) on delete cascade
    );


create table if not exists custom_schedules
(
    id bigserial primary key,
    custom_date DATE,
    timeslot_id bigint not null references timeslots(id) on delete cascade
    );
