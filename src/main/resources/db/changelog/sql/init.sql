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
    chat_id varchar(255) unique,
    name varchar(255),
    phone varchar(255) unique,
    registration_completed boolean not null
);

create table if not exists timeslots(
    id  bigserial primary key,
    start_time time,
    end_time time
);