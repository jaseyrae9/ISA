import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditRoomFormComponent } from '../edit-room-form/edit-room-form.component';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-room-basic-info',
  templateUrl: './room-basic-info.component.html',
  styleUrls: ['./room-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class RoomBasicInfoComponent implements OnInit {
  @Input() room: Room;
  @Output() roomDeleted: EventEmitter<number> = new EventEmitter();
  roles: Role[];
  forEditing: Room;
  modalRef: BsModalRef;
  hotelId: string;

  constructor(private route: ActivatedRoute, public tokenService: TokenStorageService,
     private modalService: BsModalService, private hotelService: HotelService,
     public ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    this.forEditing = new Room(this.room.id, this.room.floor, this.room.roomNumber, this.room.numberOfBeds, this.room.price,
      this.room.type);
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);

    this.hotelId = this.route.snapshot.paramMap.get('id');
  }

  openEditModal() {
    const initialState = {
      room: this.forEditing,
      hotelId: this.hotelId
    };
    this.modalRef = this.modalService.show(EditRoomFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(result => {
      this.roomEdited(result);
    });
  }

  deleteRoom() {
    this.hotelService.delete(this.room.id, this.hotelId).subscribe(
      data => {
        this.roomDeleted.emit(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  roomEdited(data) {
    console.log('Room edited');
    this.room.id = data.id;
    this.room.floor = data.floor;
    this.room.roomNumber = data.roomNumber;
    this.room.numberOfBeds = data.numberOfBeds;
    this.room.price = data.price;
    this.room.type = data.type;
    this.forEditing = new Room(this.room.id, this.room.floor, this.room.roomNumber, this.room.numberOfBeds, this.room.price,
       this.room.type);
    this.ngxNotificationService.sendMessage('Room is changed!', 'dark', 'bottom-right' );
  }

  isHotelAdmin() {
    if (this.roles !== null) {
      for (const role of this.roles) {
        if (role.authority === 'ROLE_HOTELADMIN') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

}
