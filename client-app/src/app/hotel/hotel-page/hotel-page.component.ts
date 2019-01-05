import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HotelService } from '../../services/hotel/hotel.service';
import { Hotel } from 'src/app/model/hotel/hotel';
import { Room } from 'src/app/model/hotel/room';

@Component({
  selector: 'app-hotel-page',
  templateUrl: './hotel-page.component.html',
  styleUrls: ['./hotel-page.component.css', '../../shared/css/inputField.css']
})
export class HotelPageComponent implements OnInit {

  hotel : Hotel = new Hotel();

  constructor(private route: ActivatedRoute, private hotelService: HotelService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.hotelService.get(id).subscribe(
      (data) => {
        this.hotel = data;
      }
    );
  }

  roomCreated(room: Room) {
    this.hotel.rooms.push(room);
  }
}
