import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Hotel } from 'src/app/model/hotel/hotel';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { AdditionalService } from 'src/app/model/additional-service';
import { AlertService } from 'ngx-alerts';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditServiceFormComponent } from 'src/app/components/hotel/edit-service-form/edit-service-form.component';
import { EditHotelFormComponent } from '../edit-hotel-form/edit-hotel-form.component';
import { NewRoomFormComponent } from '../new-room-form/new-room-form.component';
import { NewServiceFormComponent } from '../new-service-form/new-service-form.component';
import { formatDate } from '@angular/common';
import { Room } from 'src/app/model/hotel/room';

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

  hotelId: number;

  max = 5;
  rate = 3;
  isReadonly = false;

  hotel: Hotel = new Hotel();
  forEditing: Hotel = new Hotel();
  bsRangeValue: Date[];

  fastRooms: Room[] = [];

  modalRef: BsModalRef;

  constructor(private route: ActivatedRoute,
    private hotelService: HotelService,
    public tokenService: TokenStorageService,
    private alertService: AlertService,
    private modalService: BsModalService) {

    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });

  }

  ngOnInit() {
    const hotelId = this.route.snapshot.paramMap.get('id');
    this.hotelId = +hotelId;
    this.loadHotel();

    if (this.tokenService.isHotelAdmin && this.tokenService.companyId === this.hotelId) {
      this.hotelService.getMonthlyVisitation(hotelId).subscribe(
        (data) => {
          console.log('Prihod mesecni', data);
          this.hotelMonthlyVisitation = data;
        }
      );
    }

    if (this.tokenService.isHotelAdmin && this.tokenService.companyId === this.hotelId) {
      this.hotelService.getWeeklyVisitation(hotelId).subscribe(
        (data) => {
          console.log('Prihod nedeljni', data);
          this.hotelWeeklyVisitation = data;
        }
      );
    }

    if (this.tokenService.isHotelAdmin && this.tokenService.companyId === this.hotelId) {
      this.hotelService.getDailyVisitation(hotelId).subscribe(
        (data) => {
          console.log('Prihod dnevni', data);
          this.hotelDailyVisitation = data;
        }
      );
    }

    this.loadFastReservationRooms();
  }

  loadHotel() {
    this.hotelService.get(this.hotelId).subscribe(
      (data) => {
        this.hotel = data;
      }
    );
  }

  loadFastReservationRooms() {
    this.hotelService.getRoomsForFastReservation(this.hotelId).subscribe(
      (data) => {
        this.fastRooms = data;
        console.log('Brze su nam sobe ', data);
      }
    );
   }

  openNewRoomModal() {
    const initialState = {
      hotelId: this.hotel.id
    };
    this.modalRef = this.modalService.show(NewRoomFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(room => {
      this.alertService.info('Room ' + room.roomNumber + ' is created!');
      this.hotel.rooms.push(room);
    });
  }

  roomDeleted(roomId: number) {
    const i = this.hotel.rooms.findIndex(e => e.id === roomId);
    if (i !== -1) {
      this.hotel.rooms.splice(i, 1);
      this.alertService.info('Room is deleted!');
    }
  }

  roomSlowed(roomId: number) {
    const i = this.fastRooms.findIndex(e => e.id === roomId);
    if (i !== -1) {
      this.fastRooms.splice(i, 1);
    }
    this.loadHotel();
  }

  roomFasten(roomId: number) {
    const i = this.hotel.rooms.findIndex(e => e.id === roomId);
    if (i !== -1) {
      this.hotel.rooms.splice(i, 1);
    }
    this.loadFastReservationRooms();
  }

  // dodavanje novih dodatnih usluga
  openNewServiceModal() {
    const initialState = {
      hotelId: this.hotel.id
    };
    this.modalRef = this.modalService.show(NewServiceFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(additionalService => {
      this.hotel.additionalServices.push(additionalService);
      this.alertService.info('Service ' + additionalService.name + ' created!');
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
        this.alertService.info('Service ' + as.name + ' deleted!');
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
      this.alertService.info('Hotel is changed!');
    });
  }

  fastAdd(as: AdditionalService) {
    this.hotelService.addServiceToFast(this.hotelId, as.id).subscribe(
      data => {
        console.log('Data ', data);
        this.alertService.info('Service added to fast reservations!');
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

  fastRemove(as: AdditionalService) {
    this.hotelService.removeServiceFromFast(this.hotelId, as.id).subscribe(
      data => {
        console.log('Data ', data);
        this.alertService.info('Service removed from fast reservations!');
      },
      error => {
        console.log(error.error.message);
      }
    );
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
