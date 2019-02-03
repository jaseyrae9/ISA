import { DataService } from './../../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { User } from 'src/app/model/users/user';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-hotel-admin',
  templateUrl: './add-hotel-admin.component.html',
  styleUrls: ['./add-hotel-admin.component.css', '../../../shared/css/inputField.css']
})
export class AddHotelAdminComponent implements OnInit {
  errorMessage: String = '';

  hotels: Hotel[];
  private user: User = new User();
  newHotelAdminForm: FormGroup;

  constructor(private hotelService: HotelService,
    private dataService: DataService,
    private modalRef: BsModalRef,
    private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.hotelService.getAllHotels().subscribe(data => {
      this.hotels = data;
    });

    this.dataService.currentHotel.subscribe(hotel => {
      if (hotel.id != null) {
        this.hotels.push(hotel);
      }
    });

    this.newHotelAdminForm = this.formBuilder.group({
      email: [this.user.email, [Validators.required]],
      firstName: [this.user.firstName, [Validators.required]],
      lastName: [this.user.lastName, [Validators.required]],
      address: [this.user.address, [Validators.required]],
      phoneNumber: [this.user.phoneNumber, [Validators.required]],
      hotel: ['', [Validators.required]]
    });
  }

  onHotelAdminAdd() {
    console.log(this.newHotelAdminForm.value);

    this.user.password = 'changeme';
    this.user.email = this.newHotelAdminForm.value.email;
    this.user.firstName = this.newHotelAdminForm.value.firstName;
    this.user.lastName = this.newHotelAdminForm.value.lastName;
    this.user.address = this.newHotelAdminForm.value.address;
    this.user.phoneNumber = this.newHotelAdminForm.value.phoneNumber;

    this.hotelService.addAdmin(this.user, this.newHotelAdminForm.value.hotel).subscribe(
      data => {
        console.log(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );

  }

}
