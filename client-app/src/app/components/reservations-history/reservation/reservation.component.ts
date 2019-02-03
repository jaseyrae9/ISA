import { Component, OnInit, Input } from '@angular/core';
import { Reservation } from 'src/app/model/users/reservation';
import { formatDate } from '@angular/common';


@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {
  @Input() reservation: Reservation;
  date: String = '';


  constructor() { }

  ngOnInit() {
    console.log(this.reservation.id);

    this.date = formatDate(this.reservation.creationDate, 'yyyy-MM-dd', 'en');
  }

}
