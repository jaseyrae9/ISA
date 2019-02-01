import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { LocationService } from './../../../services/location-service';

@Component({
  selector: 'app-new-hotel-form',
  templateUrl: './new-hotel-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})


export class NewHotelFormComponent implements OnInit {
  errorMessage: String = '';
  hotel: Hotel = new Hotel();
  public onClose: Subject<Hotel>;
  newHotelForm: FormGroup;

  constructor(public modalRef: BsModalRef,
    private formBuilder: FormBuilder,
    private hotelService: HotelService,
    private locationService: LocationService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.newHotelForm = this.formBuilder.group({
      name: [this.hotel.name, [Validators.required]],
      city: [this.hotel.location.city, [Validators.required]],
      country: [this.hotel.location.country, [Validators.required]],
      address: [this.hotel.location.address,  [Validators.required]],
      description: [this.hotel.description]
      });
  }

  onHotelAdd() {
    this.locationService.decode(this.newHotelForm.value.address).subscribe(
      (data) => {
        this.hotel.location.lat = data.results[0].geometry.location.lat;
        this.hotel.location.lon = data.results[0].geometry.location.lng;
        this.add();
      },
      (error) => {
        this.hotel.location.lat = 0;
        this.hotel.location.lon = 0;
        this.add();
      }
    );
  }

  add() {
    this.hotel.name = this.newHotelForm.value.name;
    this.hotel.description = this.newHotelForm.value.description;
    this.hotel.location.address = this.newHotelForm.value.address;
    this.hotel.location.city = this.newHotelForm.value.city;
    this.hotel.location.country = this.newHotelForm.value.country;
    this.hotelService.add(this.hotel).subscribe(
      data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );
  }

}
