import { Component, OnInit, Input } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';

@Component({
  selector: 'app-room-basic-info',
  templateUrl: './room-basic-info.component.html',
  styleUrls: ['./room-basic-info.component.css']
})
export class RoomBasicInfoComponent implements OnInit {
  @Input() room : Room;

  constructor() { }

  ngOnInit() {
  }

}
