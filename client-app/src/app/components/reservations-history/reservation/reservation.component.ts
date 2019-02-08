import { Invite } from 'src/app/model/users/invite';
import { UserService } from './../../../services/user/user.service';
import { Component, OnInit, Input } from '@angular/core';
import { Reservation } from 'src/app/model/users/reservation';
import { formatDate } from '@angular/common';
import { AlertService } from 'ngx-alerts';

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

  constructor(private userService: UserService, private alertService: AlertService) { }

  ngOnInit() {
    this.date = formatDate(this.reservation.creationDate, 'yyyy-MM-dd', 'en');
    this.calculatePrice();
  }

  acceptInvite() {
    this.userService.accpetInvite(this.inviteId).subscribe(
      (data: Invite) => {
        this.reservation = data.reservationDTO;
        this.status = data.status;
        this.alertService.info('Yay, you accepted a trip invite from ' + this.invitedBy);
      },
      (error) => {
        this.alertService.info('Error occured.');
      }
    );
  }

  refuseInvite() {
    this.userService.refuseInvite(this.inviteId).subscribe(
      (data: Invite) => {
        this.reservation = data.reservationDTO;
        this.status = data.status;
        this.alertService.info('You refused to travel with ' + this.invitedBy);
        this.calculatePrice();
      },
      (error) => {
        this.alertService.info('Error occured.');
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
