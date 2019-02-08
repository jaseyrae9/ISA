import { Router } from '@angular/router';
import { Component, OnInit} from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { Hotel } from 'src/app/model/hotel/hotel';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { AddAirCompanyAdminComponent } from '../../air-company/add-air-company-admin/add-air-company-admin.component';
import { AddHotelAdminComponent } from '../../hotel/add-hotel-admin/add-hotel-admin.component';
import { AddCarAdminComponent } from '../../rent-a-car-company/add-car-admin/add-car-admin.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AddSysAdminComponent } from '../../sys-admin/add-sys-admin/add-sys-admin.component';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  public loggedIn: Boolean;
  public username: String;
  public carcompanies: RentACarCompany[];
  public hotels: Hotel[];
  public aircompanies: AirCompany[];

  modalRef: BsModalRef;

  constructor(public tokenService: TokenStorageService,
     private router: Router,
     private modalService: BsModalService) {
    this.loggedIn = tokenService.checkIsLoggedIn();
    this.tokenService.logggedInEmitter.subscribe(loggedIn => {
      this.loggedIn = loggedIn;
    });
    this.username = tokenService.getUsername();
    this.tokenService.usernameEmitter.subscribe(username => this.username = username);
  }

  ngOnInit() {
  }

  logout() {
    this.router.navigateByUrl('http://ticket-reservation21.herokuapp.com/');
    this.tokenService.signOut();
  }

  openNewAirAdminModal() {
    this.modalRef = this.modalService.show(AddAirCompanyAdminComponent);
  }

  openNewHotelAdminModal() {
    this.modalRef = this.modalService.show(AddHotelAdminComponent);
  }

  openNewCarCompanyAdminModal() {
    this.modalRef = this.modalService.show(AddCarAdminComponent);
  }

  openSysAdminModal() {
    this.modalRef = this.modalService.show(AddSysAdminComponent);
  }
}
