insert into locations(id, city, country, address, lon, lat) values (100, 'Novi Sad', 'Srbija', 'Marka Miljanova 7b',19.849669, 45.261406);
insert into locations(id, city, country, address, lon, lat) values (101, 'London', 'Velika Britanija', 'Neka ulica',-0.127758, 51.507351);
insert into locations(id, city, country, address, lon, lat) values (102, 'Novi Sad', 'Srbija', 'Jozefa Marcoka 2',19.831655, 45.236972);

--rating_count, total_rating,
insert into hotel (id, hotel_name, hotel_description, rating_count, total_rating, location_id) values (100, 'Hotel proba 1', 'Opis 1', 2, 8, 100);
insert into hotel (id, hotel_name, hotel_description, rating_count, total_rating, location_id) values (101, 'Hotel proba 2', 'Opis 2', 2, 9, 101);

insert into air_company (id, name, description, location_id) values (100, 'Aviokompanija 1', 'Opis 1', 100);
insert into air_company (id, name, description, location_id) values (101, 'Aviokompanija 2', 'Opis 2', 101);
insert into air_company (id, name, description, location_id) values (102, 'Aviokompanija 3', 'Opis 3', 102);

insert into rent_a_car_company (id, rentacar_company_name, rentacar_description, rating_count, total_rating, location_id) values (100, 'Rent a Punto', 'Grande', 2, 8, 100);
insert into rent_a_car_company (id, rentacar_company_name, rentacar_description, rating_count, total_rating,  location_id) values (101, 'Rent a Panda', 'Fiat', 3, 15, 101);
insert into rent_a_car_company (id, rentacar_company_name, rentacar_description, rating_count, total_rating, location_id) values (102, 'Rent a AUDI', 'Q7 4.2', 2, 9, 102);

INSERT INTO authority (id, name) VALUES (1, 'CUSTOMER');
INSERT INTO authority (id, name) VALUES (2, 'SYS');
INSERT INTO authority (id, name) VALUES (3, 'HOTELADMIN');
INSERT INTO authority (id, name) VALUES (4, 'AIRADMIN');
INSERT INTO authority (id, name) VALUES (5, 'CARADMIN');

INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('SYS', 1000, 'FTN, Novi Sad', true, 'admin@admin.com', 'Admin', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', false, 'admin phone', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('ACA', 1001, 'FTN, Novi Sad', true, 'airadmin@admin.com', 'Air', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', false, 'air admin phone', 100, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2000, 'Marka Miljanova 7b, Novi Sad', true, 'milicat228@gmail.com', 'Milica', 'Todorovic', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', false, '0601322175', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2001, 'Dom A, Novi Sad', true, 'majak96@gmail.com', 'Marijana', 'Kolosnjaji', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', false,'0601344575', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2002, 'Jozefa Marcoka, Novi Sad', true, 'jaseyrea9@gmail.com', 'Jelena', 'Surlan', '2018-12-26 23:09:42', '$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', false, '0605642175', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2003, 'Dom A, Novi Sad', true, 'miloskrstic@gmail.com', 'Milos', 'Krstic', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', false,'0601342565', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2004, 'Marka Miljanova 7b', true, 'lanat98@gmail.com', 'Milana', 'Todorovic', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', false, '060134565', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('HA', 3000, 'FTN, Novi Sad', true, 'krstic@admin.com', 'Admin', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', false, 'admin phone1', NULL, 100, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('RACA', 3001, 'FTN, Novi Sad', true, 'jelena@admin.com', 'Admin', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', false, 'admin phone2', NULL, NULL, 101);

INSERT INTO user_authority ( user_id, authority_id ) VALUES (1000, 2);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (1001, 4);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2000, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2001, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2002, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2003, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2004, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (3000, 3);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (3001, 5);

INSERT INTO friendships (from_id, to_id, active) VALUES (2001, 2000, false);
INSERT INTO friendships (from_id, to_id, active) VALUES (2002, 2000, false);
INSERT INTO friendships (from_id, to_id, active) VALUES (2003, 2000, false);
INSERT INTO friendships (from_id, to_id, active) VALUES (2004, 2000, false);

INSERT INTO services (id, active, description, name, price) VALUES (100, true, 'Opis usluge', 'WiFi', 5);
INSERT INTO services (id, active, description, name, price) VALUES (101, true, 'Opis usluge', 'Parking lot',  10);
INSERT INTO services (id, active, description, name, price) VALUES (102, true, 'Opis usluge', 'Air conditioning', 5);
INSERT INTO services (id, active, description, name, price) VALUES (103, true, 'Opis usluge', 'Welness', 15);
INSERT INTO services (id, active, description, name, price) VALUES (104, true, 'Opis usluge', 'Room service', 10);

insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 100);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 101);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 102);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 103);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 104);

INSERT into branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (100, true, 'Branch office name 1', 100, 100);
INSERT into branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (101, true, 'Branch office name 2', 101, 100);
INSERT into branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (102, true, 'Branch office name 3', 102, 100);

INSERT INTO room (id, active, floor, number_of_beds, price, rating_count, room_number, total_rating, type, version, hotel_id) VALUES (600, true, 4, 2, 20, 2, 20, 9, 'Regular', 0, 100);
INSERT INTO room (id, active, floor, number_of_beds, price, rating_count, room_number, total_rating, type, version, hotel_id) VALUES (601, true, 4, 2, 20, 3, 20, 15, 'Regular', 0, 100);
INSERT INTO room (id, active, floor, number_of_beds, price, rating_count, room_number, total_rating, type, version, hotel_id) VALUES (602, true, 4, 4, 25, 3, 20, 12, 'Studio', 0, 100);
INSERT INTO room (id, active, floor, number_of_beds, price, rating_count, room_number, total_rating, type, version, hotel_id) VALUES (603, true, 4, 7, 35, 2, 20, 10, 'Apartman', 0, 100);

insert into car (id, active, brand, doors_number, model, price, rating_count, seats_number, total_rating, type, year_of_production, rentacar_company_id) values (500, true, 'Fiat', 5, 'Punto 1.2', 20, 2, 5, 9, 'Sedan', 2010, 100);
insert into car (id, active, brand, doors_number, model, price, rating_count, seats_number, total_rating, type, year_of_production, rentacar_company_id) values (501, true, 'BMW', 2, 'Z4', 39, 2, 5, 8, 'Convertible', 2017, 100);
insert into car (id, active, brand, doors_number, model, price, rating_count, seats_number, total_rating, type, year_of_production, rentacar_company_id) values (502, true, 'Fiat', 7, '500L', 30, 3, 5, 15, 'Sedan', 2016, 100);
