import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { ViewChild, ElementRef} from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

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

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private hotelService: HotelService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.newHotelForm = this.formBuilder.group({
      name: [this.hotel.name, [Validators.required]],
      description: [this.hotel.description],
      });
  }

  onHotelAdd() {
    this.hotelService.add(this.newHotelForm.value).subscribe(
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

}
