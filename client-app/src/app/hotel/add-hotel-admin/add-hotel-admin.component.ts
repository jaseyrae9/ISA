import { Component, OnInit, Input } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { NgForm } from '@angular/forms';
import { User } from 'src/app/model/users/user';
import { HotelService } from 'src/app/services/hotel/hotel.service'
import { DataService } from 'src/app/shared/services/data.service'

@Component({
  selector: 'app-add-hotel-admin',
  templateUrl: './add-hotel-admin.component.html',
  styleUrls: ['./add-hotel-admin.component.css']
})
export class AddHotelAdminComponent implements OnInit {
  hotels: Hotel[];

  private user : User;

  constructor(private hotelService : HotelService,private dataService : DataService) {
  }

  ngOnInit() {
    this.hotelService.getAll().subscribe(data => {
      this.hotels = data;
    });
    this.dataService.currentHotel.subscribe(hotel => { 
      if(hotel.id != null)
      {
        this.hotels.push(hotel)
      }
    });
  }

  onSubmit(form : NgForm)
  {
    console.log(form);
    this.user = new User("changeme",form.value.firstName, form.value.lastName, form.value.email, form.value.phoneNumber, form.value.address);

    this.hotelService.addAdmin(this.user, form.value.hotel).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );

  }

}
