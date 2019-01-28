insert into locations(id, address, lon, lat) values (100, 'Marka Miljanova 7b, Novi Sad',19.849669, 45.261406);
insert into locations(id, address, lon, lat) values (101, 'London, UK',-0.127758, 51.507351);
insert into locations(id, address, lon, lat) values (102, 'Kraljice Jelene 28, Indjija',20.068591, 45.043876);

insert into hotel (id, hotel_name, hotel_description, location_id) values (100, 'Hotel proba 1', 'Opis 1', 100);
insert into hotel (id, hotel_name, hotel_description, location_id) values (101, 'Hotel proba 2', 'Opis 2', 101);

INSERT INTO services (id, active, description, name, price) VALUES (100, 1, 'Opis usluge', 'WiFi', 5);
INSERT INTO services (id, active, description, name, price) VALUES (101, 1, 'Opis usluge', 'Parking lot',  10);
INSERT INTO services (id, active, description, name, price) VALUES (102, 1, 'Opis usluge', 'Air conditioning', 5);
INSERT INTO services (id, active, description, name, price) VALUES (104, 1, 'Opis usluge', 'Welness', 15);
INSERT INTO services (id, active, description, name, price) VALUES (105, 0, 'Opis usluge', 'Room service', 10);

insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 100);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 101);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 102);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 103);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 104);
insert into hotel_additional_services (hotel_id, additional_services_id) values (100, 105);

insert into air_company (id, name, description, location_id) values (100, 'Aviokompanija 1', 'Opis 1', 100);
insert into air_company (id, name, description, location_id) values (101, 'Aviokompanija 2', 'Opis 2', 101);
insert into air_company (id, name, description, location_id) values (102, 'Aviokompanija 3', 'Opis 3', 102);

insert into rent_a_car_company (id, rentacar_company_name, rentacar_description, location_id) values (100, 'Rent a Punto', 'Grande', 100);
insert into rent_a_car_company (id, rentacar_company_name, rentacar_description, location_id) values (101, 'Rent a Panda', 'Fiat', 101);
insert into rent_a_car_company (id, rentacar_company_name, rentacar_description, location_id) values (102, 'Rent a AUDI', 'Q7 4.2', 102);

INSERT branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (100, 1, 'Branch office name 1', 100, 100);
INSERT branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (101, 1, 'Branch office name 2', 101, 100);
INSERT branch_office (id, active, branch_office_name, location_id, rentacar_company_id) VALUES (102, 1, 'Branch office name 3', 102, 100);

INSERT INTO authority (id, name) VALUES (1, 'CUSTOMER');
INSERT INTO authority (id, name) VALUES (2, 'SYS');
INSERT INTO authority (id, name) VALUES (3, 'HOTELADMIN');
INSERT INTO authority (id, name) VALUES (4, 'AIRADMIN');
INSERT INTO authority (id, name) VALUES (5, 'CARADMIN');

insert into car (id, active, brand,doors_number,model, price, seats_number, type, year_of_production, rentacar_company_id) values (500, 1, 'Fiat', 5, 'Punto 1.2', 20, 5, 'Sedan', 2010, 100);
insert into car (id, active, brand,doors_number,model, price, seats_number, type, year_of_production, rentacar_company_id) values (501, 1, 'BMW', 2, 'Z4', 39, 5, 'Convertible', 2017, 100);
insert into car (id, active, brand,doors_number,model, price, seats_number, type, year_of_production, rentacar_company_id) values (502, 1, 'Fiat', 7, '500L', 30, 5, 'Sedan', 2016, 100);

INSERT INTO car_reservation (id, active, drop_off_date, pick_up_date, car_id, customer_id, drop_off_branch_office_id,
pick_up_branch_office_id) VALUES (300, 1, '2019-02-25', '2019-01-30', 500, 2002, 100, 101);

INSERT INTO car_reservation (id, active, drop_off_date, pick_up_date, car_id, customer_id, drop_off_branch_office_id,
pick_up_branch_office_id) VALUES (301, 1, '2019-02-18', '2019-01-30', 501, 2002, 100, 101);

INSERT INTO room (id, active, floor, number_of_beds, price, room_number, type, hotel_id) VALUES (600, 1, 4, 2, 20, 20, 'Regular', 100);
INSERT INTO room (id, active, floor, number_of_beds, price, room_number, type, hotel_id) VALUES (601, 0, 4, 2, 20, 20, 'Regular', 100);
INSERT INTO room (id, active, floor, number_of_beds, price, room_number, type, hotel_id) VALUES (602, 1, 4, 4, 25, 20, 'Studio', 100);
INSERT INTO room (id, active, floor, number_of_beds, price, room_number, type, hotel_id) VALUES (603, 1, 4, 7, 35, 20, 'Apartman', 100);


INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('SYS', 1000, 'FTN, Novi Sad', 1, 'admin@admin.com', 'Admin', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', 0, 'admin phone', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('ACA', 1001, 'FTN, Novi Sad', 1, 'airadmin@admin.com', 'Air', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', 0, 'air admin phone', 100, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2000, 'Marka Miljanova 7b, Novi Sad', 1, 'milicat228@gmail.com', 'Milica', 'Todorovic', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', 0, '0601322175', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2001, 'Dom A, Novi Sad', 1, 'majak96@gmail.com', 'Marijana', 'Kolosnjaji', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', 0,'0601344575', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2002, 'Jozefa Marcoka, Novi Sad', 1, 'jaseyrea9@gmail.com', 'Jelena', 'Surlan', '2018-12-26 23:09:42', '$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', 0, '0605642175', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2003, 'Dom A, Novi Sad', 1, 'miloskrstic@gmail.com', 'Milos', 'Krstic', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', 0,'0601342565', NULL, NULL, NULL);
INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('CUST', 2004, 'Marka Miljanova 7b', 1, 'lanat98@gmail.com', 'Milana', 'Todorovic', '2018-12-26 23:09:42','$2a$10$QHSpHaeAyVOiqKEf5WnaK.tME9/IuK6RTwMicLrrdqCc9i1zDrX3y', 0, '060134565', NULL, NULL, NULL);

INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('HA', 3000, 'FTN, Novi Sad', 1, 'krstic@admin.com', 'Admin', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', 0, 'admin phone1', NULL, 100, NULL);

INSERT INTO users (type, id, address, confirmed_mail, email, first_name, last_name, last_password_reset_date, password, needs_password_change, phone_number, air_company_id, hotel_id, rent_a_car_company_id)
values ('RACA', 3001, 'FTN, Novi Sad', 1, 'jelena@admin.com', 'Admin', 'Admin', '2018-12-26 23:09:42','$2a$10$QQxHVraAtUHQqf266vLzfuNLsF5XVS7W4AnJatRZR2gtQpk1LMD0K', 0, 'admin phone2', NULL, NULL, 100);


INSERT INTO user_authority ( user_id, authority_id ) VALUES (1000, 2);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (1001, 4);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2000, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2001, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2002, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2003, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (2004, 1);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (3000, 3);
INSERT INTO user_authority ( user_id, authority_id ) VALUES (3001, 5);

INSERT INTO friendships (from_id, to_id, active) VALUES (2001, 2000, 0);
INSERT INTO friendships (from_id, to_id, active) VALUES (2002, 2000, 0);
INSERT INTO friendships (from_id, to_id, active) VALUES (2003, 2000, 0);
INSERT INTO friendships (from_id, to_id, active) VALUES (2004, 2000, 0);

INSERT INTO room_reservation (id, active, check_in_date, check_out_date, customer_id) VALUES (180, 1, '2019-01-30', '2019-02-05', 2001);
INSERT INTO room_reservation (id, active, check_in_date, check_out_date, customer_id) VALUES (181, 1, '2019-01-15', '2019-01-28', 2001);

INSERT INTO single_room_reservation (id, room_id, room_reservation_id) VALUES (400, 600, 180);
INSERT INTO single_room_reservation (id, room_id, room_reservation_id) VALUES (401, 600, 181);


