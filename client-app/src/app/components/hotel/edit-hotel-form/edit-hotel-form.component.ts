import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { Hotel } from 'src/app/model/hotel/hotel';
import { ViewChild, ElementRef } from '@angular/core';


@Component({
  selector: 'app-edit-hotel-form',
  templateUrl: './edit-hotel-form.component.html',
  styleUrls: ['./edit-hotel-form.component.css', '../../../shared/css/inputField.css']
})
export class EditHotelFormComponent implements OnInit {
  @Output() hotelEdited: EventEmitter<Hotel> = new EventEmitter();
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() hotel: Hotel;

  constructor(private hotelService: HotelService) { }

  ngOnInit() {
  }

  onHotelEdit() {
    this.hotelService.edit(this.hotel).subscribe(
      data => {
        console.log("Stigao odgovor", data);
        this.hotelEdited.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}