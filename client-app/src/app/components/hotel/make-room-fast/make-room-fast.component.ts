import { Component, OnInit, Input } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Room } from 'src/app/model/hotel/room';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { NgxNotificationService } from 'ngx-notification';
import { Subject } from 'rxjs/Subject';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { HttpErrorResponse } from '@angular/common/http';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-make-room-fast',
  templateUrl: './make-room-fast.component.html',
  styleUrls: ['./make-room-fast.component.css', '../../../shared/css/inputField.css']
})
export class MakeRoomFastComponent implements OnInit {
  @Input() room: Room;
  @Input() hotelId;

  makeFastRoomForm: FormGroup;

  datePickerConfig: Partial<BsDatepickerConfig>;
  bsRangeValue: Date[];

  errorMessage: String = '';

  public onClose: Subject<Room>;

  constructor(private formBuilder: FormBuilder,
    public modalRef: BsModalRef,
    public ngxNotificationService: NgxNotificationService,
    private hotelService: HotelService) {
    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
    this.onClose = new Subject();
    this.makeFastRoomForm = this.formBuilder.group({
      discount: ['', [Validators.min(0)]],
      bsRangeValue: [this.bsRangeValue, [Validators.required]],
    });

  }

  onfastRoomAdd() {
    const date0 = new Date(this.makeFastRoomForm.value.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    const date1 = new Date(this.makeFastRoomForm.value.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');


    this.hotelService.makeRoomFast(this.hotelId, this.room.id, datum0,
       datum1, this.makeFastRoomForm.value.discount ).subscribe(
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
