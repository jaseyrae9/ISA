import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Hotel } from 'src/app/model/hotel/hotel';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private hotel = new BehaviorSubject<Hotel>(new Hotel());
  currentHotel = this.hotel.asObservable();

  private aircompany = new BehaviorSubject<AirCompany>(new AirCompany());
  currentAirCompany = this.aircompany.asObservable();

  private carCompany = new BehaviorSubject<RentACarCompany>(new RentACarCompany());
  currentCarCompany = this.carCompany.asObservable();

  constructor() { }

  changeHotel(h: Hotel) {
    this.hotel.next(h);
  }

  changeAirCompany(ac: AirCompany) {
    this.aircompany.next(ac);
  }

  changeCarCompany(cc: RentACarCompany) {
    this.carCompany.next(cc);
  }

}
