INSERT INTO customer_service_group (name) VALUES ('Стрижка');
INSERT INTO customer_service_group (name) VALUES ('Борода');
INSERT INTO customer_service_group (name) VALUES ('Комплекс');
INSERT INTO customer_service_group (name) VALUES ('Камуфляж');
INSERT INTO customer_service_group (name) VALUES ('Другие');

INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Стрижка детская ', 4000, (select id from customer_service_group where name='Стрижка'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Стрижка мужская', 7000, (select id from customer_service_group where name='Стрижка'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Стрижка машинкой', 1500, (select id from customer_service_group where name='Стрижка'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Коррекция бороды', 3000, (select id from customer_service_group where name='Борода'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Королевское бритье', 7000, (select id from customer_service_group where name='Борода'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (120, 'Стрижка+Борода', 9000, (select id from customer_service_group where name='Комплекс'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Камуфляж бороды', 2000, (select id from customer_service_group where name='Камуфляж'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Камуфляж головы', 3000, (select id from customer_service_group where name='Камуфляж'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Окантовка', 2000, (select id from customer_service_group where name='Другие'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Удаление волос воском', 1000, (select id from customer_service_group where name='Другие'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES (60, 'Чистка лица(скраб+глина)', 2000, (select id from customer_service_group where name='Другие'));
