drop table if exists customer_service_group, customer_service cascade;

create table if not exists customer_service_group
(
    id bigserial primary key,
    name varchar(255) not null unique
);


create table if not exists customer_service
(
    id                        bigserial
        primary key,
    duration                  varchar(255) not null,
    name                      varchar(255) not null,
    price                     varchar(255) not null,
    customer_service_group_id bigint not null
        constraint fk_customer_service_customer_service_group
            references public.customer_service_group
);


