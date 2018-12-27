import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../services/hotel/hotel.service';
import { Hotel } from 'src/app/model/hotel/hotel';

@Component({
  selector: 'app-all-hotels-page',
  templateUrl: './all-hotels-page.component.html',
  styleUrls: ['./all-hotels-page.component.css']
})
export class AllHotelsPageComponent implements OnInit {
  hotels: Hotel[];

  constructor(private hotelService: HotelService) { }

  ngOnInit() {
    this.hotelService.getAll().subscribe(data => {
      this.hotels = data;
    });
  }

}
