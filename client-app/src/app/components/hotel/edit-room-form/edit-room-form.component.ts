import { Component, OnInit, Input } from '@angular/core';
import { Room } from 'src/app/model/hotel/room';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';

@Component({
  selector: 'app-edit-room-form',
  templateUrl: './edit-room-form.component.html',
  styleUrls: ['./edit-room-form.component.css', '../../../shared/css/inputField.css']
})
export class EditRoomFormComponent implements OnInit {
  @Input() room: Room;
  @Input() hotelId: string;

  public onClose: Subject<Room>;

  constructor(private hotelService: HotelService, public modalRef: BsModalRef) { }

  ngOnInit() {
    this.onClose = new Subject();
  }

  onRoomEdit() {
    this.hotelService.editRoom(this.room, this.hotelId).subscribe(
      data => {
        this.onClose.next(this.room);
        this.modalRef.hide();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
