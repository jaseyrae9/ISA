import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { ViewChild, ElementRef } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

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

  constructor(private formBuilder: FormBuilder, public modalRef: BsModalRef, private hotelService: HotelService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editHotelForm = this.formBuilder.group({
      id: [this.hotel.id],
      name: [this.hotel.name, [Validators.required]],
      description: [this.hotel.description],
      });
  }

  onEditHotel() {
    this.hotelService.edit(this.editHotelForm.value).subscribe(
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
