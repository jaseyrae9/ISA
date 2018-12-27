import { Component, OnInit } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';

@Component({
  selector: 'app-new-hotel-form',
  templateUrl: './new-hotel-form.component.html',
  styleUrls: ['./new-hotel-form.component.css']
})
export class NewHotelFormComponent implements OnInit {
  hotel: Hotel;

  constructor() { }

  ngOnInit() {
  }

}
