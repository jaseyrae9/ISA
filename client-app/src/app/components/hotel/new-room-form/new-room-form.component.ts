import { Component, OnInit, Input, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { ActivatedRoute } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-room-form',
  templateUrl: './new-room-form.component.html',
  styleUrls: ['./new-room-form.component.css', '../../../shared/css/inputField.css']
})
export class NewRoomFormComponent implements OnInit {
  errorMessage: String = '';
  room: Room = new Room();
  @Input() hotelId: Number;

  public onClose: Subject<Room>;
  newRoomForm: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder,
    private route: ActivatedRoute, private hotelService: HotelService) { }

  ngOnInit() {
    this.room.type = 'Regular';
    this.onClose = new Subject();
    this.newRoomForm = this.formBuilder.group({
      floor: [this.room.floor, [Validators.required]],
      roomNumber: [this.room.roomNumber, [Validators.required, Validators.min(0)]],
      type: [this.room.type],
      numberOfBeds: [this.room.numberOfBeds, [Validators.required], Validators.min[1]],
      price: [this.room.price, [Validators.required, Validators.min(0)]],
    });
  }

  onRoomAdd() {
        this.hotelService.addRoom(this.newRoomForm.value, this.hotelId).subscribe(
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
