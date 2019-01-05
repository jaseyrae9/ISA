import { Component, OnInit, Input } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';

@Component({
  selector: 'app-car-basic-info',
  templateUrl: './car-basic-info.component.html',
  styleUrls: ['./car-basic-info.component.css', '../../shared/css/inputField.css']
})
export class CarBasicInfoComponent implements OnInit {
  @Input() car : Car;

  constructor() { }

  ngOnInit() {
  }

}
