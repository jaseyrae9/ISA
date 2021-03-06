import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditRoomFormComponent } from '../edit-room-form/edit-room-form.component';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { AlertService } from 'ngx-alerts';
import { HttpErrorResponse } from '@angular/common/http';
import { MakeRoomFastComponent } from '../make-room-fast/make-room-fast.component';

@Component({
  selector: 'app-room-basic-info',
  templateUrl: './room-basic-info.component.html',
  styleUrls: ['./room-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class RoomBasicInfoComponent implements OnInit {
  @Input() room: Room;
  @Input() bookedRooms: Room[] = [];
  @Output() roomDeleted: EventEmitter<number> = new EventEmitter();
  @Output() roomFasten: EventEmitter<number> = new EventEmitter();

  modalRef: BsModalRef;
  hotelId: number;

  @Input() isRoomsTab = true;

  isBooked = false;
  @Output() roomBooked: EventEmitter<Room> = new EventEmitter();
  @Output() roomUnbooked: EventEmitter<Room> = new EventEmitter();


  constructor(private route: ActivatedRoute, public tokenService: TokenStorageService,
     private modalService: BsModalService, private hotelService: HotelService,
     private alertService: AlertService) { }

  ngOnInit() {
    this.hotelId = +this.route.snapshot.paramMap.get('id');
    console.log('Init component for room' + this.room.id);
    const index: number = this.bookedRooms.indexOf(this.room);
    if (index !== -1) {
      this.isBooked = true;
    }
  }

  // izmena sobe
  openEditModal() {
    const initialState = {
      room: this.room,
      hotelId: this.hotelId
    };
    this.modalRef = this.modalService.show(EditRoomFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(room => {
      this.room = room;
      this.alertService.info('Room is changed!');
    });
  }

  // Fast reservation
  openFastModal() {
    const initialState = {
      room: this.room,
      hotelId: this.hotelId
    };
    this.modalRef = this.modalService.show(MakeRoomFastComponent, { initialState });
    this.modalRef.content.onClose.subscribe(room => {
      this.room = room;
      console.log('Fasten ', room);
      this.roomFasten.emit(room.id);
      this.alertService.info('Room is added to fast!');
    });
  }

  deleteRoom() {
    this.hotelService.delete(this.room.id, this.hotelId).subscribe(
      data => {
        this.roomDeleted.emit(data);
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.alertService.info(err.error.details);
      }
    );
  }

  bookRoom() {
    this.isBooked = true;
    this.roomBooked.emit(this.room);
  }

  unbookRoom() {
    this.isBooked = false;
    this.roomUnbooked.emit(this.room);
  }

}
