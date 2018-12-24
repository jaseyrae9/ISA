import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../services/hotel/hotel.service';

@Component({
  selector: 'app-all-hotels-page',
  templateUrl: './all-hotels-page.component.html',
  styleUrls: ['./all-hotels-page.component.css']
})
export class AllHotelsPageComponent implements OnInit {
  hotels: Array<any>;

  constructor(private hotelService: HotelService) { }

  ngOnInit() {
    this.hotelService.getAll().subscribe(data => {
      this.hotels = data;
    });
  }

}
