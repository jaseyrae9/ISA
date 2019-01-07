import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { ViewChild, ElementRef} from '@angular/core';

@Component({
  selector: 'app-new-hotel-form',
  templateUrl: './new-hotel-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})


export class NewHotelFormComponent implements OnInit {
  @Output() hotelCreated: EventEmitter<Hotel> = new EventEmitter();
  form: any = {};
  hotel: Hotel;
  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(private hotelService: HotelService) { }

  ngOnInit() {
  }

  onHotelAdd() {
    this.hotel = new Hotel(null,
      this.form.name,
      this.form.description
    );

    this.hotelService.add(this.hotel).subscribe(
      data => {
        this.hotelCreated.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );


  }

}
