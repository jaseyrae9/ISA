import { Component, OnInit, Input } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';

@Component({
  selector: 'app-room-basic-info',
  templateUrl: './room-basic-info.component.html',
  styleUrls: ['./room-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class RoomBasicInfoComponent implements OnInit {
  @Input() room : Room;
  roles: Role[];
  forEditing : Room;

  constructor(public tokenService: TokenStorageService) { }

  ngOnInit() {
    this.forEditing = new Room(this.room.id, this.room.floor, this.room.roomNumber, this.room.numberOfBeds, this.room.price);
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  roomEdited(data)
  {
    console.log('Room edited');
    this.room.id = data.id;
    this.room.floor = data.floor;
    this.room.roomNumber = data.roomNumber;
    this.room.numberOfBeds = data.numberOfBeds;
    this.room.price = data.price;
    this.forEditing = new Room(this.room.id, this.room.floor, this.room.roomNumber, this.room.numberOfBeds, this.room.price);
  }

  isHotelAdmin() {
    if (this.roles != undefined) {
      for (let role of this.roles) {
        if (role.authority == 'ROLE_HOTELADMIN') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

}
