import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Hotel } from 'src/app/model/hotel/hotel';
import { Room } from 'src/app/model/hotel/room';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';


@Component({
  selector: 'app-hotel-page',
  templateUrl: './hotel-page.component.html',
  styleUrls: ['./hotel-page.component.css', '../../../shared/css/inputField.css']
})
export class HotelPageComponent implements OnInit {

  hotel: Hotel = new Hotel();
  forEditing: Hotel = new Hotel();
  roles: Role[];

  constructor(private route: ActivatedRoute, private hotelService: HotelService, public tokenService: TokenStorageService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.hotelService.get(id).subscribe(
      (data) => {
        this.hotel = data;
        this.forEditing = new Hotel(data.id, data.name, data.description, data.rooms);
      }
    );
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  hotelEdited(data) {
    this.hotel.id = data.id;
    this.hotel.name = data.name;
    this.hotel.description = data.description;
    this.forEditing = new Hotel(data.id, data.name, data.description, data.rooms);
  }

  roomCreated(room: Room) {
    this.hotel.rooms.push(room);
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
