import { Component, OnInit, Input } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';

@Component({
  selector: 'app-hotel-basic-details',
  templateUrl: './hotel-basic-details.component.html',
  styleUrls: ['./hotel-basic-details.component.css']
})
export class HotelBasicDetailsComponent implements OnInit {
  @Input() hotel: Hotel;

  constructor() { }

  ngOnInit() {
  }

}
