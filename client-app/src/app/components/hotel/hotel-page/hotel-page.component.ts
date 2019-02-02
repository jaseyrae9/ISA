import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Hotel } from 'src/app/model/hotel/hotel';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { AdditionalService } from 'src/app/model/additional-service';
import { NgxNotificationService } from 'ngx-notification';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditServiceFormComponent } from 'src/app/components/hotel/edit-service-form/edit-service-form.component';
import { EditHotelFormComponent } from '../edit-hotel-form/edit-hotel-form.component';
import { NewRoomFormComponent } from '../new-room-form/new-room-form.component';
import { NewServiceFormComponent } from '../new-service-form/new-service-form.component';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-hotel-page',
  templateUrl: './hotel-page.component.html',
  styleUrls: ['./hotel-page.component.css', '../../../shared/css/inputField.css']
})
export class HotelPageComponent implements OnInit {
  hotelMonthlyVisitation: any;
  hotelWeeklyVisitation: any;
  hotelDailyVisitation: any;

  income: any;

  datePickerConfig: Partial<BsDatepickerConfig>;

  hotelId: string;

  max = 5;
  rate = 3;
  isReadonly = false;

  hotel: Hotel = new Hotel();
  forEditing: Hotel = new Hotel();
  bsRangeValue: Date[];

  modalRef: BsModalRef;

  constructor(private route: ActivatedRoute,
    private hotelService: HotelService,
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
    const hotelId = this.route.snapshot.paramMap.get('id');
    this.hotelId = hotelId;
    this.hotelService.get(hotelId).subscribe(
      (data) => {
        this.hotel = data;
        console.log('Otvoren je hotel: ', this.hotel);
      }
    );
    if (this.tokenService.isHotelAdmin) {
      this.hotelService.getMonthlyVisitation(hotelId).subscribe(
        (data) => {
          console.log('Prihod mesecni', data);
          this.hotelMonthlyVisitation = data;
        }
      );
    }

    if (this.tokenService.isHotelAdmin) {
      this.hotelService.getWeeklyVisitation(hotelId).subscribe(
        (data) => {
          console.log('Prihod nedeljni', data);
          this.hotelWeeklyVisitation = data;
        }
      );
    }

    if (this.tokenService.isHotelAdmin) {
      this.hotelService.getDailyVisitation(hotelId).subscribe(
        (data) => {
          console.log('Prihod dnevni', data);
          this.hotelDailyVisitation = data;
        }
      );
    }
  }

  openNewRoomModal() {
    const initialState = {
      hotelId: this.hotel.id
    };
    this.modalRef = this.modalService.show(NewRoomFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(room => {
      this.ngxNotificationService.sendMessage('Room ' + room.roomNumber + ' is created!', 'dark', 'bottom-right');
      this.hotel.rooms.push(room);
    });
  }

  roomDeleted(roomId: number) {
    const i = this.hotel.rooms.findIndex(e => e.id === roomId);
    if (i !== -1) {
      this.hotel.rooms.splice(i, 1);
      this.ngxNotificationService.sendMessage('Room is deleted!', 'dark', 'bottom-right');
    }
  }

  // dodavanje novih dodatnih usluga
  openNewServiceModal() {
    const initialState = {
      hotelId: this.hotel.id
    };
    this.modalRef = this.modalService.show(NewServiceFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(additionalService => {
      this.hotel.additionalServices.push(additionalService);
      this.ngxNotificationService.sendMessage('Service ' + additionalService.name + ' created!', 'dark', 'bottom-right');
    });
  }

  // editvanje dodatnih usluga
  editClicked(as: AdditionalService) {
    console.log('editing as', as);

    const initialState = {
      additionalService: as,
      hotelId: this.hotel.id
    };
    this.modalRef = this.modalService.show(EditServiceFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(result => {
      console.log('result', result);
      const i = this.hotel.additionalServices.findIndex(e => e.id === result.id);
      if (i !== -1) {
        this.hotel.additionalServices.splice(i, 1, result);
      }
    });
  }

  deleteClicked(as: AdditionalService) {
    this.hotelService.deleteAdditionalService(as.id, this.hotel.id).subscribe(
      data => {
        const i = this.hotel.additionalServices.findIndex(e => e.id === data);
        if (i !== -1) {
          this.hotel.additionalServices.splice(i, 1);
        }
        this.ngxNotificationService.sendMessage('Service ' + as.name + ' deleted!', 'dark', 'bottom-right');
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

  // editing hotel
  openEditModal() {
    const initialState = {
      hotel: this.hotel,
      hotelId: this.hotelId
    };
    this.modalRef = this.modalService.show(EditHotelFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(hotel => {
      this.hotel = hotel;
      this.ngxNotificationService.sendMessage('Hotel is changed!', 'dark', 'bottom-right');
    });
  }

  getIncome() {

    const date0 = new Date(this.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    console.log(datum0);
    const date1 = new Date(this.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');
    console.log(datum1);

    this.hotelService.getIncome(this.hotel.id, datum0, datum1).subscribe(
      data => {
        this.income = data;
      },
      error => {
        console.log(error.error.message);
      }
    );
  }
}
