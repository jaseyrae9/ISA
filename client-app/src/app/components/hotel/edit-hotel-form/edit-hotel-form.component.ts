import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit, Input } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { LocationService } from 'src/app/services/location-service';
import { Location } from 'src/app/model/location';

@Component({
  selector: 'app-edit-hotel-form',
  templateUrl: './edit-hotel-form.component.html',
  styleUrls: ['./edit-hotel-form.component.css', '../../../shared/css/inputField.css']
})
export class EditHotelFormComponent implements OnInit {
  errorMessage: String = '';
  public onClose: Subject<Hotel>;
  @Input() hotel: Hotel;
  editHotelForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
    public modalRef: BsModalRef,
    private hotelService: HotelService,
    private locationService: LocationService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editHotelForm = this.formBuilder.group({
      id: [this.hotel.id],
      name: [this.hotel.name, [Validators.required]],
      city: [this.hotel.location.city, [Validators.required]],
      country: [this.hotel.location.country, [Validators.required]],
      address: [this.hotel.location.address, [Validators.required]],
      description: [this.hotel.description],
    });
  }

  submit(value) {
    this.hotelService.edit(value).subscribe(
      data => {
        this.onClose.next(data);
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

  onSubmit() {
    const value = this.editHotelForm.value;
    value.location = new Location();
    if (this.addressChanged(value)) {
      value.location.address = value.address;
      value.location.city = value.city;
      value.location.country = value.country;
      this.locationService.decode(value.address + ',+' + value.city + ',+' + value.conutry).subscribe(
        (data) => {
          value.location.lat = data.results[0].geometry.location.lat;
          value.location.lon = data.results[0].geometry.location.lng;
          this.submit(value);
        },
        (error) => {
          console.log(error);
          value.location.lat = 0;
          value.location.lon = 0;
          this.submit(value);
        }
      );
    } else {
      // ovde ce uci samo edit, pa ako vec nisu menjali nista, da ne trazimo opet od googla koordinate
      value.location = this.hotel.location;
      this.submit(value);
    }
  }

  addressChanged(value): boolean {
    return value.address !== this.hotel.location.address || value.city !== this.hotel.location.city
      || value.country !== this.hotel.location.country;
  }

}
