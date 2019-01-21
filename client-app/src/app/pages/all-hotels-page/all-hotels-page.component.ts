import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../services/hotel/hotel.service';
import { Hotel } from 'src/app/model/hotel/hotel';
import { NgxNotificationService } from 'ngx-notification';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';

@Component({
  selector: 'app-all-hotels-page',
  templateUrl: './all-hotels-page.component.html',
  styleUrls: ['./all-hotels-page.component.css', '../../shared/css/inputField.css']
})
export class AllHotelsPageComponent implements OnInit {
  hotels: Hotel[];
  roles: Role[];

  constructor(private hotelService: HotelService, private dataService: DataService, public tokenService: TokenStorageService,
    public ngxNotificationService: NgxNotificationService) {

  }

  ngOnInit() {
    this.hotelService.getAll().subscribe(data => {
      this.hotels = data;
    });
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  hotelCreated(hotel: Hotel) {
    this.hotels.push(hotel);
    this.dataService.changeHotel(hotel);
    this.ngxNotificationService.sendMessage(hotel.name + ' is created!', 'dark', 'bottom-right' );

  }

  isSysAdmin() {
    if (this.roles !== null) {
      for (const role of this.roles) {
        if (role.authority === 'ROLE_SYS') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

}
