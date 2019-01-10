import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { Room } from 'src/app/model/hotel/room'
import { ActivatedRoute } from '@angular/router';
import { HotelService } from 'src/app/services/hotel/hotel.service';

@Component({
  selector: 'app-edit-room-form',
  templateUrl: './edit-room-form.component.html',
  styleUrls: ['./edit-room-form.component.css', '../../../shared/css/inputField.css']
})
export class EditRoomFormComponent implements OnInit {
  @Output() roomEdited: EventEmitter<Room> = new EventEmitter();
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() room: Room;
  hotelId: string;

  constructor(private route: ActivatedRoute, private hotelService: HotelService) { }

  ngOnInit() {
    console.log("soba" + this.room.type);
    const hotelId = this.route.snapshot.paramMap.get('id');
    this.hotelId = hotelId;
  }

  onRoomEdit() {
    this.hotelService.editRoom(this.room, this.hotelId).subscribe(
      data => {
        this.roomEdited.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
