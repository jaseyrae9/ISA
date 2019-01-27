import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../services/hotel/hotel.service';
import { Hotel } from 'src/app/model/hotel/hotel';
import { NgxNotificationService } from 'ngx-notification';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NewHotelFormComponent } from 'src/app/components/hotel/new-hotel-form/new-hotel-form.component';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { FormBuilder, FormGroup } from '@angular/forms';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-all-hotels-page',
  templateUrl: './all-hotels-page.component.html',
  styleUrls: ['./all-hotels-page.component.css', '../../shared/css/inputField.css']
})
export class AllHotelsPageComponent implements OnInit {

  hotels: Hotel[] = [];
  modalRef: BsModalRef;

  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  searchHotelForm: FormGroup;
  bsRangeValue: Date[];

  constructor(private formBuilder: FormBuilder,
    private hotelService: HotelService,
    private dataService: DataService,
    public tokenService: TokenStorageService,
    public ngxNotificationService: NgxNotificationService,
    private modalService: BsModalService) {
    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
    this.hotelService.getAll().subscribe(data => {
      this.hotels = data;
    });

    this.searchHotelForm = this.formBuilder.group({
      hotelName: [''],
      bsRangeValue: [this.bsRangeValue],
      hotelAddress: [''],
    });
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

  openNewHotelModal() {
    this.modalRef = this.modalService.show(NewHotelFormComponent);
    this.modalRef.content.onClose.subscribe(hotel => {
      this.ngxNotificationService.sendMessage(hotel.name + ' is created!', 'dark', 'bottom-right');
      this.hotels.push(hotel);
      this.dataService.changeHotel(hotel);
    });
  }
}
