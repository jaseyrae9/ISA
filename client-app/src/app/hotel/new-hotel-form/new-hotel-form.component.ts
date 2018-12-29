import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { HotelService } from '../../services/hotel/hotel.service';

@Component({
  selector: 'app-new-hotel-form',
  templateUrl: './new-hotel-form.component.html',
  styleUrls: ['./new-hotel-form.component.css']
})


export class NewHotelFormComponent implements OnInit {
  @Output() hotelCreated: EventEmitter<Hotel> = new EventEmitter();
  
  // public button: Button;
  form: any = {};
  hotel: Hotel;

  constructor(private hotelService: HotelService) { }

  ngOnInit() {
  }

  onHotelAdd()
  {
    console.log("Adding hotel");
    console.log(this.form.description);

    this.hotel = new Hotel(null,
      this.form.name,
      this.form.description
    );

    this.hotelService.add(this.hotel).subscribe(
      data => {
        console.log("uspesno dodat hotel");
        this.hotelCreated.emit(data);
        
        let element : HTMLElement = document.getElementById('closeButton') as HTMLElement;
        element.click();

      },
      error => {
        console.log(error.error.message);
      }
    );


  }

}
