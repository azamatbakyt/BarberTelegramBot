drop table if exists customer_service_group, customer_service, users, time_slots, schedules cascade;

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

create table if not exists users
(
    id         bigserial
        primary key,
    chat_id bigserial
        unique,
    name       varchar(255),
    phone      varchar(255)
        unique
);


create table if not exists users
(
    id bigserial primary key,
    chat_id bigserial unique,
    name varchar(255),
    phone varchar(255) unique
    );

--
-- create table if not exists time_slots
-- (
--     timeslot_id bigserial primary key,
--     "date" varchar(100),
--     time varchar(100)
--     );


-- create table if not exists schedules(
--                                         schedule_id bigserial primary key,
--                                         user_id bigint not null references users(chat_id) on delete cascade,
--     day_of_week varchar(100),
--     timeslot_id bigint references time_slots(timeslot_id) on delete cascade
--     );
--
--
-- create table if not exists appointments(
--                                            appointment_id bigserial primary key,
--                                            user_id bigint not null references users(id) on delete cascade,
--     service_id bigint not null references customer_service(id) on delete cascade,
--     timeslot_id bigint not null references time_slots(timeslot_id) on delete cascade,
--     appointment_date varchar(100) not null
--     );
--
-- create table if not exists custom_schedules(
--                                                custom_schedule_id bigserial primary key,
--                                                user_id bigint not null references users(id) on delete cascade,
--     "date" varchar(100) not null,
--     timeslot_id bigint not null references time_slots(timeslot_id) on delete cascade
--     );
--





