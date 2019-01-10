import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-new-room-form',
  templateUrl: './new-room-form.component.html',
  styleUrls: ['./new-room-form.component.css', '../../../shared/css/inputField.css']
})
export class NewRoomFormComponent implements OnInit {
  @Output() roomCreated: EventEmitter<Room> = new EventEmitter();
  form: any = {};
  @ViewChild('closeBtn') closeBtn: ElementRef;
  room: Room;
  id: string;


  constructor(private route: ActivatedRoute, private hotelService : HotelService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.id = id;
    this.form.type = 'Regular';
  }

  onRoomAdd() {
    this.room = new Room(null,
      this.form.floor,
      this.form.roomNumber,
      this.form.numberOfBeds,
      this.form.price,
      this.form.type
      );


    this.hotelService.addRoom(this.room, this.id).subscribe(
      data => {
        this.roomCreated.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }

    );
  }

}
