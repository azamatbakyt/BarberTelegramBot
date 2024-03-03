INSERT INTO timeslot (id, start_time, end_time) VALUES (3, '10:00:00', '11:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (4, '11:00:00', '12:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (5, '12:00:00', '13:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (6, '13:00:00', '14:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (7, '14:00:00', '15:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (8, '15:00:00', '16:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (9, '16:00:00', '17:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (10, '17:00:00', '18:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (11, '18:00:00', '19:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (12, '19:00:00', '20:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (13, '20:00:00', '21:00:00');
INSERT INTO timeslot (id, start_time, end_time) VALUES (14, '00:00:00', '00:00:00');


SELECT setval('timeslot_id_seq', COALESCE( (SELECT MAX(id)+1 FROM timeslot), 1), false);