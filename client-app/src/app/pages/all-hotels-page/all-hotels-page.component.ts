import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../services/hotel/hotel.service';
import { Hotel } from 'src/app/model/hotel/hotel';
import * as decode from 'jwt-decode';
import { TokenPayload } from 'src/app/model/token-payload';

@Component({
  selector: 'app-all-hotels-page',
  templateUrl: './all-hotels-page.component.html',
  styleUrls: ['./all-hotels-page.component.css', '../../shared/css/inputField.css']
})
export class AllHotelsPageComponent implements OnInit {
  hotels: Hotel[];
  tokenPayload : TokenPayload;

  constructor(private hotelService: HotelService, private dataService: DataService) 
  {
    
  }

  ngOnInit() {
    this.hotelService.getAll().subscribe(data => {
      this.hotels = data;
    });
    const token =  sessionStorage.getItem('AuthToken');
    // decode the token to get its payload
    const tokenPayload : TokenPayload = decode(token);
    this.tokenPayload = tokenPayload;
  }

  hotelCreated(hotel: Hotel) {
    this.hotels.push(hotel);
    this.dataService.changeHotel(hotel);
  }

  isSysAdmin()
  {
    for(let role of this.tokenPayload.roles)
    {
      console.log(role.authority);
      if(role.authority == 'ROLE_SYS')
      {
        return true;
      }
    }
    return false;
  }
  
}
