import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { HotelService } from '../../services/hotel/hotel.service';
import { Hotel } from 'src/app/model/hotel/hotel';
import { AlertService } from 'ngx-alerts';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NewHotelFormComponent } from 'src/app/components/hotel/new-hotel-form/new-hotel-form.component';

@Component({
  selector: 'app-all-hotels-page',
  templateUrl: './all-hotels-page.component.html',
  styleUrls: ['./all-hotels-page.component.css', '../../shared/css/inputField.css']
})
export class AllHotelsPageComponent implements OnInit {
  sort  = 'name';
  pages: Array<Number> = new Array();
  pageNumber = 0;

  hotels: Hotel[] = [];
  modalRef: BsModalRef;

  constructor(private hotelService: HotelService,
    private dataService: DataService,
    public tokenService: TokenStorageService,
    private alertService: AlertService,
    private modalService: BsModalService) { }

  ngOnInit() {
    this.loadHotels();
  }

  onSortChange(value) {
    this.sort = value;
    this.loadHotels();
  }

  loadHotels() {
    this.hotelService.getAll(this.pageNumber, this.sort).subscribe(data => {
      console.log(data);
      this.hotels = data['content'];
      this.pages = new Array(data['totalPages']);
    });
  }

  openNewHotelModal() {
    this.modalRef = this.modalService.show(NewHotelFormComponent);
    this.modalRef.content.onClose.subscribe(hotel => {
      this.alertService.info(hotel.name + ' is created!');
      this.loadHotels();
      this.dataService.changeHotel(hotel);
    });
  }

  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i < this.pages.length) {
      this.pageNumber += i;
      this.loadHotels();
    }
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.loadHotels();
  }
}
