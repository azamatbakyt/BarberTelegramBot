drop table if exists customer_service_group, customer_service, clients, timeslot,
    schedule, appointment, custom_schedule, appointment_timeslot cascade;

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
    duration bigint not null,
    name varchar(255) not null,
    price bigint not null,
    customer_service_group_id bigint not null
    constraint fk_customer_service_customer_service_group
    references customer_service_group
    );



create table if not exists client
(
    id bigserial primary key,
    chat_id bigserial unique,
    name varchar(255),
    phone varchar(255) unique,
    registration_completed boolean not null
);

create table if not exists timeslot(
    id  bigserial primary key,
    start_time time,
    end_time time,

    unique(start_time, end_time)
);

create table if not exists schedule(
    id bigserial primary key,
    day_of_week varchar(25) ,
    timeslot_id bigint not null references timeslot(id) on delete cascade,
    unique(day_of_week, timeslot_id)
    );


create table if not exists custom_schedule
(
    id bigserial primary key,
    custom_date DATE,
    timeslot_id bigint not null references timeslot(id) on delete cascade,
    unique(custom_date, timeslot_id)
    );

create table if not exists appointment(
    id bigserial primary key,
    client_id bigint not null references client(id) on delete cascade,
    service_id bigint not null references customer_service(id) on delete cascade,
    date_of_booking DATE
);

create table if not exists appointment_timeslot(
    id bigserial primary key,
    appointment_id bigint not null references appointment(id) on delete cascade,
    timeslot_id bigint not null references timeslot(id) on delete cascade,
    unique(appointment_id, timeslot_id)
);
