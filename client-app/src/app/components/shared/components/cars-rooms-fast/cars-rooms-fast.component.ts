import { Component, OnInit, Input } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { Room } from 'src/app/model/hotel/room';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-cars-rooms-fast',
  templateUrl: './cars-rooms-fast.component.html',
  styleUrls: ['./cars-rooms-fast.component.css', '../../../../shared/css/inputField.css']
})
export class CarsRoomsFastComponent implements OnInit {

  cars: Car[] = [];
  rooms: Room[] = [];

  constructor(private hotelService: HotelService,
    private carService: RentACarCompanyService,
    private route: ActivatedRoute) { }

  ngOnInit() {
    const city = this.route.snapshot.paramMap.get('city');
    const date = this.route.snapshot.paramMap.get('date');
    const ticketCount = this.route.snapshot.paramMap.get('ticketCount');

    this.carService.getFastCarsSearch(city, date).subscribe(
      (data) => {
        console.log('AUTO MO BILI: ', data);
        this.cars = data;
      }
    );

    this.hotelService.getFastRoomsSearch(city, date, ticketCount).subscribe(
      (data) => {
        this.rooms = data;
      }
    );
  }

}
