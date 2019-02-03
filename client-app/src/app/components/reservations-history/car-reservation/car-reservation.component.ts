import { Component, OnInit, Input } from '@angular/core';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { formatDate } from '@angular/common';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';

@Component({
  selector: 'app-car-reservation',
  templateUrl: './car-reservation.component.html',
  styleUrls: ['./car-reservation.component.css']
})
export class CarReservationComponent implements OnInit {
  @Input() reservation: CarReservation;


  max = 5;
  rateCompany = 3;
  rateCar = 2;

  diff: number;
  totalPrice: number;
  pickUp = '';
  dropOff = '';
  date0 = new Date();
  date1 = new Date();
  // this.date = formatDate(this.reservation.creationDate, 'yyyy-MM-dd', 'en');
  constructor(private carService: RentACarCompanyService) { }

  ngOnInit() {
    console.log('AAA');
    this.pickUp = formatDate(this.reservation.pickUpDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    this.dropOff = formatDate(this.reservation.dropOffDate, 'yyyy-MM-dd hh:mm:ss', 'en');
    this.date0 = new Date(this.pickUp);
    this.date1 = new Date(this.dropOff);
    this.diff =  (this.date1.getTime() - this.date0.getTime()) / (1000 * 60 * 60 * 24) + 1 ;
    console.log('razlikaaa', this.diff);
    this.totalPrice = this.diff * this.reservation.car.price;
  }

  rateCarCompany() {
    console.log('aaa vise');
    console.log('dsad' + this.rateCompany);

    this.reservation.isCompanyRated = true;
    // this.carService.rateCarCompany(this.rateCompany, this.reservation.id).

  }
}
