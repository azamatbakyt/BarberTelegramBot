INSERT INTO customer_service_group (name) VALUES ('Стрижка');
INSERT INTO customer_service_group (name) VALUES ('Борода');
INSERT INTO customer_service_group (name) VALUES ('Комплекс');
INSERT INTO customer_service_group (name) VALUES ('Камуфляж');
INSERT INTO customer_service_group (name) VALUES ('Другие');

INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Стрижка детская ', '4000тг', (select id from customer_service_group where name='Стрижка'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Стрижка мужская', '7000тг', (select id from customer_service_group where name='Стрижка'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Стрижка машинкой', '1500тг', (select id from customer_service_group where name='Стрижка'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Коррекция бороды', '3000тг', (select id from customer_service_group where name='Борода'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Королевское бритье', '7000тг', (select id from customer_service_group where name='Борода'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('2 часа', 'Стрижка+Борода', '9000тг', (select id from customer_service_group where name='Комплекс'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Камуфляж бороды', '2000тг', (select id from customer_service_group where name='Камуфляж'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Камуфляж головы', '3000тг', (select id from customer_service_group where name='Камуфляж'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Окантовка', '2000тг', (select id from customer_service_group where name='Другие'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Удаление волос воском', 'от 1000тг', (select id from customer_service_group where name='Другие'));
INSERT INTO customer_service (duration, name, price, customer_service_group_id) VALUES ('1 час', 'Чистка лица(скраб+глина)', '2000тг', (select id from customer_service_group where name='Другие'));
