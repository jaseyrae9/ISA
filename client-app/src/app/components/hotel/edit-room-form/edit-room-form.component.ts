import { Component, OnInit, Input } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-edit-room-form',
  templateUrl: './edit-room-form.component.html',
  styleUrls: ['./edit-room-form.component.css', '../../../shared/css/inputField.css']
})
export class EditRoomFormComponent implements OnInit {
  @Input() room: Room;
  @Input() hotelId: string;
  errorMessage: String = '';
  editRoomForm: FormGroup;
  public onClose: Subject<Room>;

  constructor(private formBuilder: FormBuilder,
    private hotelService: HotelService,
    public modalRef: BsModalRef) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editRoomForm = this.formBuilder.group({
      id: [this.room.id],
      floor: [this.room.floor, [Validators.required]],
      roomNumber: [this.room.roomNumber, [Validators.required, Validators.min(0)]],
      type: [this.room.type],
      numberOfBeds: [this.room.numberOfBeds, [Validators.required], Validators.min[1]],
      price: [this.room.price, [Validators.required, Validators.min(0)]],
    });
  }

  onEditRoom() {
    this.hotelService.editRoom(this.editRoomForm.value, this.hotelId).subscribe(
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
