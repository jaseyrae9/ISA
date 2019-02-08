import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AlertService } from 'ngx-alerts';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { RoomReservation } from 'src/app/model/hotel/room-reservation';
import { FlightReservation } from './../../../../model/air-company/flight-reservation';
import { ShoppingCartService } from './../../../../observables/shopping-cart.service';
import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { ReservationsService } from 'src/app/services/reservations.service';

const FLIGHT_KEY = 'flight_in_cart';
const HOTEL_KEY = 'hotel_in_cart';
const CAR_KEY = 'car_in_cart';


@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {
  flightReservation: FlightReservation = null;
  roomReservation: RoomReservation = null;
  carReservation: CarReservation = null;

  constructor(
    public tokenService: TokenStorageService,
    private shoppingCartService: ShoppingCartService,
    private alertService: AlertService,
    public datePipe: DatePipe,
    private reservationsService: ReservationsService,
    private router: Router
  ) { }

  ngOnInit() {

    if (sessionStorage.getItem(FLIGHT_KEY)) {
      this.flightReservation = JSON.parse(sessionStorage.getItem(FLIGHT_KEY));
      console.log(this.flightReservation);
    }

    if (sessionStorage.getItem(HOTEL_KEY)) {
      this.roomReservation = JSON.parse(sessionStorage.getItem(HOTEL_KEY));
    }

    if (sessionStorage.getItem(CAR_KEY)) {
      this.carReservation = JSON.parse(sessionStorage.getItem(CAR_KEY));
    }

    this.tokenService.logggedInEmitter.subscribe(
      data => {
        if (!data) {
          this.clearCart();
        }
      }
    );

    this.shoppingCartService.currentFlightReservation.subscribe(data => {
      if (data) {
        if (this.flightReservation !== null) {
          this.alertService.info('You already have a ticket reservation!');
          return;
        }
        this.flightReservation = data;
        this.alertService.info('Flight reservation added to cart!');
        window.sessionStorage.setItem(FLIGHT_KEY, JSON.stringify(data));
      }
    });

    this.shoppingCartService.currentRoomReservation.subscribe(data => {
      if (data) {
        if (this.roomReservation !== null) {
          this.alertService.info('You already have a hotel reservation!');
          return;
        }
        this.roomReservation = data;
        this.alertService.info('Hotel reservation added to cart!');
        window.sessionStorage.setItem(HOTEL_KEY, JSON.stringify(data));
      }
    });

    this.shoppingCartService.currentCarReservation.subscribe(data => {
      if (data) {
        if (this.carReservation !== null) {
          this.alertService.info('You already have a car reservation!');
          return;
        }
        this.carReservation = data;
        this.alertService.info('Car reservation added to cart!');
        window.sessionStorage.setItem(CAR_KEY, JSON.stringify(data));
      }
    });
  }

  removeFlightReservation() {
    this.flightReservation = null;
    window.sessionStorage.removeItem(FLIGHT_KEY);
  }

  removeRoomReservation() {
    this.roomReservation = null;
    window.sessionStorage.removeItem(HOTEL_KEY);
  }

  removeCarReservation() {
    this.carReservation = null;
    window.sessionStorage.removeItem(CAR_KEY);
  }

  clearCart() {
    this.removeFlightReservation();
    this.removeRoomReservation();
    this.removeCarReservation();
  }

  buy() {
    const reservationDTO = {
      flightReservationRequest: this.createFlightReservationDTO(this.flightReservation),
      carReservationRequest: this.createCarReservationDTO(this.carReservation),
      hotelReservationRequest: this.createRoomReservationDTO(this.roomReservation)
    };
    this.reservationsService.reserve(reservationDTO).subscribe(
      (data) => {
        this.clearCart();
        this.alertService.info('Tickets reserved.');
        this.router.navigate(['/history']);
      },
      (error) => {
        this.alertService.info(error.error.details);
        this.clearCart();
      }
    );
  }

  createFlightReservationDTO(flightReservation: FlightReservation) {
    if (flightReservation !== null) {
      const flightReservationDTO = {
        flightId: flightReservation.flight.id,
        ticketReservations: flightReservation.ticketReservations.map(
          function (ticket) {
            return {
              firstName: ticket.firstName,
              lastName: ticket.lastName,
              passport: ticket.passport,
              status: ticket.status,
              ticketId: ticket.ticket.id,
              friendId: ticket.friend_id
            };
          })
      };
      return flightReservationDTO;
    }
  }

  createCarReservationDTO(carReservation: CarReservation) {
    if (carReservation !== null) {
      const carReservationDTO = {
        carId: carReservation.car.id,
        pickUpBranchOffice: carReservation.pickUpBranchOffice.id,
        dropOffBranchOffice: carReservation.dropOffBranchOffice.id,
        pickUpDate: carReservation.pickUpDate,
        dropOffDate: carReservation.dropOffDate,
        isFastReservation: carReservation.isFastReservation
      };
      return carReservationDTO;
    }
  }

  createRoomReservationDTO(roomReservation: RoomReservation) {
    if (roomReservation !== null) {
      const roomReservationDTO = {
        hotelId: roomReservation.hotel.id,
        checkInDate: roomReservation.checkInDate,
        checkOutDate: roomReservation.checkOutDate,
        rooms: [],
        additionalServices: [],
        isFastReservation: roomReservation.isFastReservation
      };


      for (const room of roomReservation.reservations) {
        roomReservationDTO.rooms.push(room.id);
      }

      for (const as of roomReservation.additionalServices) {
        roomReservationDTO.additionalServices.push(as.id);
      }

      return roomReservationDTO;
    }
  }
}
