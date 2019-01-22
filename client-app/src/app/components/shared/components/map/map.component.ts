import { Location } from './../../../../model/location';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  @Input() location: Location = new Location();

  constructor() { }

  ngOnInit() {
  }

}
