insert into locations(id, city, country, address, lon, lat) values (100, 'Novi Sad', 'Srbija', 'Marka Miljanova 7b',19.849669, 45.261406);
insert into locations(id, city, country, address, lon, lat) values (101, 'London', 'Velika Britanija', 'Neka ulica',-0.127758, 51.507351);
insert into locations(id, city, country, address, lon, lat) values (102, 'Novi Sad', 'Srbija', 'Jozefa Marcoka 2',19.831655, 45.236972);

insert into hotel (id, hotel_name, hotel_description, rating_count, total_rating, location_id) values (100, 'Hotel proba 1', 'Opis 1', 2, 8, 100);
insert into hotel (id, hotel_name, hotel_description, rating_count, total_rating, location_id) values (101, 'Hotel proba 2', 'Opis 2', 2, 9, 101);

insert into air_company (id, name, description, location_id, rating_count, total_rating) values (100, 'Aviokompanija 1', 'Opis 1', 100, 1, 5);
insert into air_company (id, name, description, location_id, rating_count, total_rating) values (101, 'Aviokompanija 2', 'Opis 2', 101, 1, 5);
insert into air_company (id, name, description, location_id, rating_count, total_rating) values (102, 'Aviokompanija 3', 'Opis 3', 102, 1, 5);

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
values ('RACA', 3001, 'FTN, Novi Sad', true, 'jelena@admin.com', 'Admin', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', false, 'admin phone2', NULL, NULL, 100);

INSERT INTO user_authority ( user_id, authority_id ) VALUES (1000, 2);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (1001, 4);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2000, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2001, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2002, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2003, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2004, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (3000, 3);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (3001, 5);

INSERT INTO friendships (from_id, to_id, active) VALUES (2001, 2000, true);
INSERT INTO friendships (from_id, to_id, active) VALUES (2002, 2000, true);
INSERT INTO friendships (from_id, to_id, active) VALUES (2003, 2000, false);
INSERT INTO friendships (from_id, to_id, active) VALUES (2004, 2000, false);

INSERT INTO services (id, active, description, is_fast, name, price) VALUES (100, true, 'Opis usluge', false, 'WiFi', 5);
INSERT INTO services (id, active, description, is_fast, name, price) VALUES (101, true, 'Opis usluge', false, 'Parking lot',  10);
INSERT INTO services (id, active, description, is_fast, name, price) VALUES (102, true, 'Opis usluge', false, 'Air conditioning', 5);
INSERT INTO services (id, active, description, is_fast, name, price) VALUES (103, true, 'Opis usluge', false, 'Welness', 15);
INSERT INTO services (id, active, description, is_fast, name, price) VALUES (104, true, 'Opis usluge', false, 'Room service', 10);

insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 100);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 101);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 102);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 103);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 104);

INSERT into branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (100, true, 'Branch office name 1', 100, 100);
INSERT into branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (101, true, 'Branch office name 2', 101, 100);
INSERT into branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (102, true, 'Branch office name 3', 102, 100);

	
INSERT INTO room (id, active, begin_date, discount, end_date, floor, is_fast, number_of_beds, price, rating_count, room_number, total_rating, type, hotel_id) 
VALUES (600, true, null, 0, null, 4, false, 2, 20, 2, 20, 9, 'Regular', 100);
INSERT INTO room (id, active, begin_date, discount, end_date, floor, is_fast, number_of_beds, price, rating_count, room_number, total_rating, type, hotel_id) 
VALUES (601, true, null, 0, null, 4, false, 2, 20, 3, 20, 15, 'Regular', 100);
INSERT INTO room (id, active, begin_date, discount, end_date, floor, is_fast, number_of_beds, price, rating_count, room_number, total_rating, type, hotel_id) 
VALUES (602, true, null, 0, null, 4, false, 4, 25, 3, 20, 12, 'Studio', 100);
INSERT INTO room (id, active, begin_date, discount, end_date, floor, is_fast, number_of_beds, price, rating_count, room_number, total_rating, type, hotel_id) 
VALUES (603, true, null, 0, null, 4, false, 7, 35, 2, 20, 10, 'Apartman', 100);

insert into car (id, active, begin_date, brand, discount, doors_number, end_date, fast_drop_off_branch_office, fast_pick_up_branch_office, is_fast, model, price, rating_count, seats_number, total_rating, type, year_of_production, rentacar_company_id) values (500, true, null, 'Fiat', 0.0, 5, null, null, null, false, 'Punto 1.2', 20, 2, 5, 9, 'Sedan', 2010, 100);
insert into car (id, active, begin_date, brand, discount, doors_number, end_date, fast_drop_off_branch_office, fast_pick_up_branch_office, is_fast, model, price, rating_count, seats_number, total_rating, type, year_of_production, rentacar_company_id) values (501, true, null, 'BMW', 0.0, 2, null, null, null, false, 'Z4', 39, 2, 5, 8, 'Convertible', 2017, 100);
insert into car (id, active, begin_date, brand, discount, doors_number, end_date, fast_drop_off_branch_office, fast_pick_up_branch_office, is_fast, model, price, rating_count, seats_number, total_rating, type, year_of_production, rentacar_company_id) values (502, true, '2019-03-05','Fiat', 10.0, 7, '2019-04-05', 100, 101, true, '500L', 30, 3, 5, 15, 'Sedan', 2016, 100);

INSERT INTO car_reservation(id, active, drop_off_date, is_car_rated, is_company_rated, pick_up_date, car_id, drop_off_branch_office_id, pick_up_branch_office_id)
VALUES (300, true, '2019-02-25', true, true, '2019-01-30', 500, 100, 101);

INSERT INTO reservation(id, creation_date, car_reservation_id, customer_id, flight_reservation_id, room_reservation_id) 
VALUES (100, '2018-11-21', 300, 2002, null, null);


--INSERT INTO room_reservation(id, active, check_in_date, check_out_date, is_hotel_rated, reservation_id) VALUES (180, true, '2019-01-30', '2019-02-05', true, 102);
--INSERT INTO room_reservation(id, active, check_in_date, check_out_date, is_hotel_rated, reservation_id) VALUES (181, true, '2019-01-15', '2019-01-28', false, 103);

--INSERT INTO single_room_reservation (id, is_room_rated, room_id, room_reservation_id) VALUES (400, false, 600, 180);
--INSERT INTO single_room_reservation (id, is_room_rated, room_id, room_reservation_id) VALUES (401, false, 600, 181);
--INSERT INTO single_room_reservation (id, is_room_rated, room_id, room_reservation_id) VALUES (402, false, 603, 181);

-- Destinacije
INSERT INTO destinations(id, active, airport_name, city, country, label, air_company_id)
VALUES (1100, true, 'Nikola Tesla', 'Beograd', 'Srbija', 'JAT', 100);
INSERT INTO destinations(id, active, airport_name, city, country, label, air_company_id)
VALUES (1101, true, 'MilicaTodo', 'Milici', 'Republika Srpska', 'MIL', 100);




	



