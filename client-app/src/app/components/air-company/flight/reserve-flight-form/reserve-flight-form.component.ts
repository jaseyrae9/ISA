import { ReservationsService } from './../../../../services/reservations.service';
import { ShoppingCartService } from './../../../../observables/shopping-cart.service';
import { Friendship } from 'src/app/model/users/friendship';
import { InviteAFriendComponent } from './../invite-a-friend/invite-a-friend.component';
import { TicketReservation } from './../../../../model/air-company/ticket-reservation';
import { FlightReservation } from './../../../../model/air-company/flight-reservation';
import { ReservationInformationComponent } from './../reservation-information/reservation-information.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Ticket } from './../../../../model/air-company/ticket';
import { Flight } from './../../../../model/air-company/flight';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit, ViewChild } from '@angular/core';
import { TicketsDisplayComponent } from '../tickets-display/tickets-display.component';
import { AlertService } from 'ngx-alerts';

@Component({
  selector: 'app-reserve-flight-form',
  templateUrl: './reserve-flight-form.component.html',
  styleUrls: ['./reserve-flight-form.component.css', './../../../../shared/css/inputField.css']
})
export class ReserveFlightFormComponent implements OnInit {
  reservation: FlightReservation = new FlightReservation();
  errorMessage: String = '';
  flight: Flight = new Flight();
  page = 0;
  modalRef: BsModalRef;
  @ViewChild('tickets') ticketsView: TicketsDisplayComponent;

  constructor(private router: Router, private route: ActivatedRoute, private airService: AirCompanyService,
    private modalService: BsModalService, private shoppingCartService: ShoppingCartService,
    private reservationsService: ReservationsService, private alertService: AlertService) { }

  ngOnInit() {
    this.loadFlight();
  }

  loadFlight() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airService.getFlight(id).subscribe(
      (data) => {
        this.reservation.flight = data;
      }
    );
  }

  pageOne() {
    let total = 0;
    const tickets = this.ticketsView.checkedTickets;
    if (tickets.length === 0) {
      this.errorMessage = 'Please, select at least one ticket.';
      return;
    }
    for (const ticket of tickets) {
      const ticketReservation = new TicketReservation();
      ticketReservation.ticket = ticket;
      this.reservation.ticketReservations.push(ticketReservation);
      total += ticket.price;
    }
    this.reservation.total = total;
    this.page = 1;
  }

  pageTwo() {
    const self = false;
    for (const ticketReservation of this.reservation.ticketReservations) {
      if (ticketReservation.status === -1) {
        this.errorMessage = 'Please, fill in data for all seats.';
        return;
      }
    }
    this.page = 2;
  }

  useYourData(item: TicketReservation) {
    const initialState = {
      isFastReservation: true
    };
    this.modalRef = this.modalService.show(ReservationInformationComponent, { initialState });
    this.modalRef.content.onClose.subscribe(value => {
      item.status = 0;
      item.passport = value.passport;
    });
  }

  fillInData(item: TicketReservation) {
    const initialState = {
      isFastReservation: false
    };
    this.modalRef = this.modalService.show(ReservationInformationComponent, { initialState });
    this.modalRef.content.onClose.subscribe(value => {
      item.status = 1;
      item.firstName = value.firstName;
      item.lastName = value.lastName;
      item.passport = value.passport;
    });
  }

  inviteAFriend(item: TicketReservation) {
    const initialState = {
      reservation: this.reservation
    };
    this.modalRef = this.modalService.show(InviteAFriendComponent, { initialState });
    this.modalRef.content.onClose.subscribe( (value: Friendship) => {
      item.status = 2;
      item.firstName = value.user2Firstname;
      item.lastName = value.user2Lastname;
      item.friend_id = value.user2Id;
    });
  }

  yesClick() {
    this.shoppingCartService.changeFlightReservation(this.reservation);
    const city = this.reservation.flight.destinations[this.reservation.flight.destinations.length - 1].destination.city;
    // tslint:disable-next-line:max-line-length
    const route = '/cars-and-hotels/' + city + '/' + this.reservation.flight.endDateAndTime + '/' + this.reservation.ticketReservations.length;
    this.router.navigate([route]);
  }

  noClick() {
    const reservationDTO = {
      flightReservationRequest: this.createFlightReservationDTO(this.reservation)
    };
    this.reservationsService.reserve(reservationDTO).subscribe(
      (data) => {
        this.alertService.info('Tickets reserved.');
        this.router.navigate(['/history']);
      },
      (error) => {
        this.reservation = new FlightReservation();
        this.loadFlight();
        this.page = 0;
        this.errorMessage = error.error.details;
      }
    );
  }

  createFlightReservationDTO(flightReservation: FlightReservation) {
    const flightReservationDTO = {
      flightId: flightReservation.flight.id,
      ticketReservations: flightReservation.ticketReservations.map(
                          function(ticket) {return {
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


