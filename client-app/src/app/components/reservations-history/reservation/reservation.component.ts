import { Invite } from 'src/app/model/users/invite';
import { UserService } from './../../../services/user/user.service';
import { Component, OnInit, Input } from '@angular/core';
import { Reservation } from 'src/app/model/users/reservation';
import { formatDate } from '@angular/common';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css', '../../../shared/css/inputField.css']
})
export class ReservationComponent implements OnInit {
  @Input() reservation: Reservation;
  @Input() inviteId: number;
  @Input() status = 'PENDING';
  @Input() invitedBy = '';
  date: String = '';

  totalPrice = 0;

  constructor(private userService: UserService, private ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    this.date = formatDate(this.reservation.creationDate, 'yyyy-MM-dd', 'en');
    this.calculatePrice();
  }

  acceptInvite() {
    this.userService.accpetInvite(this.inviteId).subscribe(
      (data: Invite) => {
        this.reservation = data.reservationDTO;
        this.status = data.status;
        this.ngxNotificationService.sendMessage('Yay, you accepted a trip invite from ' + this.invitedBy, 'dark', 'bottom-right');
      },
      (error) => {
        this.ngxNotificationService.sendMessage('Error occured.', 'dark', 'bottom-right');
      }
    );
  }

  refuseInvite() {
    this.userService.refuseInvite(this.inviteId).subscribe(
      (data: Invite) => {
        this.reservation = data.reservationDTO;
        this.status = data.status;
        this.ngxNotificationService.sendMessage('You refused to travel with ' + this.invitedBy, 'dark', 'bottom-right');
        this.calculatePrice();
      },
      (error) => {
        this.ngxNotificationService.sendMessage('Error occured.', 'dark', 'bottom-right');
      }
    );
  }

  calculatePrice() {
    this.totalPrice = 0;
    if (this.reservation.flightReservation !== null) {
      this.totalPrice += this.reservation.flightReservation.total;
    }
    if (this.reservation.roomReservation !== null) {
      this.totalPrice += this.reservation.roomReservation.total;
    }
    if (this.reservation.carReservation !== null) {
      this.totalPrice += this.reservation.carReservation.total;
    }
  }

  onTripCanceled(data) {
    this.reservation = data;
  }

}
