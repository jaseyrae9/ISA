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

  constructor(private route: ActivatedRoute,
    private hotelService: HotelService,
    public tokenService: TokenStorageService,
    public ngxNotificationService: NgxNotificationService) {

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
  }

  roomCreated(room: Room) {
    console.log(room);
    this.hotel.rooms.push(room);
  }

  roomDeleted(roomId: number) {
    const i = this.hotel.rooms.findIndex(e => e.id === roomId);
    if (i !== -1) {
      this.hotel.rooms.splice(i, 1);
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

    this.hotelService.editAdditionalService(as, this.hotel.id).subscribe(
      data => {
      },
      error => {
        console.log(error.error.message);
      }
    );
   }

  deleteClicked(as: AdditionalService) {
    console.log('delete as', as);

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
