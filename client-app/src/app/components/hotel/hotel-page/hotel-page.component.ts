import { HotelService } from 'src/app/services/hotel/hotel.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Hotel } from 'src/app/model/hotel/hotel';
import { Room } from 'src/app/model/hotel/room';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';
import { AdditionalService } from 'src/app/model/additional-service';
import { NgxNotificationService } from 'ngx-notification';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditServiceFormComponent } from 'src/app/components/hotel/edit-service-form/edit-service-form.component';

@Component({
  selector: 'app-hotel-page',
  templateUrl: './hotel-page.component.html',
  styleUrls: ['./hotel-page.component.css', '../../../shared/css/inputField.css']
})
export class HotelPageComponent implements OnInit {
  datePickerConfig: Partial<BsDatepickerConfig>;

  max = 5;
  rate = 3;
  isReadonly = false;

  hotel: Hotel = new Hotel();
  forEditing: Hotel = new Hotel();
  roles: Role[];
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
        dateInputFormat: 'DD/MM/YYYY'
      });

  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.hotelService.get(id).subscribe(
      (data) => {
        this.hotel = data;
        console.log(this.hotel);
        this.forEditing = new Hotel(data.id, data.name, data.description);
      }
    );
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  hotelEdited(data) {
    this.hotel.id = data.id;
    this.hotel.name = data.name;
    this.hotel.description = data.description;
    this.forEditing = new Hotel(data.id, data.name, data.description);
    this.ngxNotificationService.sendMessage('Hotel is changed!', 'dark', 'bottom-right');
  }

  roomCreated(room: Room) {
    console.log(room);
    this.hotel.rooms.push(room);
    this.ngxNotificationService.sendMessage('Room ' + room.roomNumber + ' is created!', 'dark', 'bottom-right');
  }

  roomDeleted(roomId: number) {
    const i = this.hotel.rooms.findIndex(e => e.id === roomId);
    if (i !== -1) {
      this.hotel.rooms.splice(i, 1);
      this.ngxNotificationService.sendMessage('Room is deleted!', 'dark', 'bottom-right');
    }
  }

  isHotelAdmin() {
    if (this.roles !== null) {
      for (const role of this.roles) {
        if (role.authority === 'ROLE_HOTELADMIN') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

  additionalServiceCreated(additionalService: AdditionalService) {
    console.log('Service created', additionalService);
    this.hotel.additionalServices.push(additionalService);
    this.ngxNotificationService.sendMessage('Service ' + additionalService.name + ' created!', 'dark', 'bottom-right');
  }

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

}
