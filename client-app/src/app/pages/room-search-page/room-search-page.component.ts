import { Component, OnInit } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { FormGroup, FormBuilder } from '@angular/forms';
import { formatDate } from '@angular/common';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Hotel } from 'src/app/model/hotel/hotel';

@Component({
  selector: 'app-room-search-page',
  templateUrl: './room-search-page.component.html',
  styleUrls: ['./room-search-page.component.css', '../../shared/css/inputField.css']
})
export class RoomSearchPageComponent implements OnInit {

  hotels: Hotel[] = [];

  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  searchHotelForm: FormGroup;
  bsRangeValue: Date[];

  constructor(private formBuilder: FormBuilder, private hotelService: HotelService) {
    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
    this.searchHotelForm = this.formBuilder.group({
      hotelName: [''],
      bsRangeValue: [this.bsRangeValue],
      hotelAddress: [''],
    });
    this.onSearch();
  }

  onSearch() {
    console.log('pretraga');

    let date0 = '';
    let date1 = '';
    if (this.searchHotelForm.value.bsRangeValue !== null) {
      date0 = formatDate(this.searchHotelForm.value.bsRangeValue[0], 'yyyy-MM-dd', 'en');
      date1 = formatDate(this.searchHotelForm.value.bsRangeValue[1], 'yyyy-MM-dd', 'en');
    }


    this.hotelService.getSearchAll(this.searchHotelForm.value.hotelName, this.searchHotelForm.value.hotelAddress,
      date0, date1).subscribe(data => {
        this.hotels = data;
      });

  }

}
