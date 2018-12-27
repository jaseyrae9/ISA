INSERT INTO authority (id, name) VALUES (1, 'CUSTOMER');
INSERT INTO authority (id, name) VALUES (2, 'SYS');
INSERT INTO authority (id, name) VALUES (3, 'HOTELADMIN');
INSERT INTO authority (id, name) VALUES (4, 'AVIOADMIN');
INSERT INTO authority (id, name) VALUES (5, 'CARADMIN');

INSERT INTO user_authority ( user_id, authority_id ) VALUES (1000, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2000, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (1001, 2);

insert into hotel (id, hotel_name, hotel_description) values (100, 'Hotel proba 1', 'Opis 1');
insert into hotel (id, hotel_name, hotel_description) values (101, 'Hotel proba 2', 'Opis 2');

insert into air_company (id, name, description) values (100, 'Aviokompanija 1', 'Opis 1');
insert into air_company (id, name, description) values (101, 'Aviokompanija 2', 'Opis 2');
insert into air_company (id, name, description) values (102, 'Aviokompanija 3', 'Opis 3');

insert into rent_a_car_company (id, rentacar_company_name, rentacar_description) values (100, 'Rent a Punto', 'Grande');
insert into rent_a_car_company (id, rentacar_company_name, rentacar_description) values (101, 'Rent a Panda', 'Fiat');
insert into rent_a_car_company (id, rentacar_company_name, rentacar_description) values (102, 'Rent a AUDI', 'Q7 4.2');

INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, phone_number, username, air_company_id, hotel_id, rent_a_car_company_id)
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, phone_number, username, air_company_id, hotel_id, rent_a_car_company_id)
VALUES ('CUST', 1000, 'Korisnik', 0, 'korisnik@korisnik.com', 'korisnik', 'korisnik', '2018-12-26 23:09:42', '$2a$10$fRXwtOGnubnw09NqaK38cuP2GOQIm0DoNL2OkucjMH1S.dCn6meWK', '021212', 'korisnik', NULL, NULL, NULL);
VALUES ('CUST', 2000, 'Marka Miljanova 7b, Novi Sad', 1, 'milicat228@gmail.com', 'Milica', 'Todorovic', '2018-12-26 23:09:42', '$2a$10$fRXwtOGnubnw09NqaK38cuP2GOQIm0DoNL2OkucjMH1S.dCn6meWK', '03221212', 'milicat228', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, phone_number, username, air_company_id, hotel_id, rent_a_car_company_id)
VALUES ('SYS', 1001, 'adresaa', 0, 'sys@sys.com', 'sys', 'sys', '2018-12-26 23:20:39', '$2a$10$S0H6hQLlgJru7tSHW7Znw.KCf43WwBHt2qPo5a9Mh1Rk04tVHPm0i', '23232', 'sys', NULL, NULL, NULL);