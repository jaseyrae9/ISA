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
    private modalService: BsModalService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airService.getFlight(id).subscribe(
      (data) => {
        this.reservation.flight = data;
      }
    );
  }

  pageOne() {
    const tickets = this.ticketsView.checkedTickets;
    if (tickets.length === 0) {
      this.errorMessage = 'Please, select at least one ticket.';
      return;
    }
    for (const ticket of tickets) {
      const ticketReservation = new TicketReservation();
      ticketReservation.ticket = ticket;
      this.reservation.ticketReservations.push(ticketReservation);
    }
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
}


