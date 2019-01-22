import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { NgxNotificationService } from 'ngx-notification';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NewCarFormComponent } from '../new-car-form/new-car-form.component';
import { NewBranchOfficeFormComponent } from '../new-branch-office-form/new-branch-office-form.component';

@Component({
  selector: 'app-car-company-page',
  templateUrl: './car-company-page.component.html',
  styleUrls: ['./car-company-page.component.css', '../../../shared/css/inputField.css']
})
export class CarCompanyPageComponent implements OnInit {
  modalRef: BsModalRef;
  carCompany: RentACarCompany = new RentACarCompany();
  forEditing: RentACarCompany = new RentACarCompany();

  constructor(private modalService: BsModalService,
     private route: ActivatedRoute,
     private carService: RentACarCompanyService,
     public tokenService: TokenStorageService,
     public ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.carService.get(id).subscribe(
      (data) => {
        this.carCompany = data;
        console.log('Aaa', data);
        this.forEditing = new RentACarCompany(data.id, data.name, data.description, data.cars);

      }
    );
  }

  carCompanyEdited(data) {
    this.carCompany.id = data.id;
    this.carCompany.name = data.name;
    this.carCompany.description = data.description;
    this.forEditing = new RentACarCompany(data.id, data.name, data.description, data.cars);
    this.ngxNotificationService.sendMessage('Rent a car company is changed!', 'dark', 'bottom-right' );
  }

  carDeleted(car: Car) {
    console.log('Parent: Car deleted', car);
    const index: number = this.carCompany.cars.indexOf(car);
    if (index !== -1) {
      this.carCompany.cars.splice(index, 1);
      this.ngxNotificationService.sendMessage(car.model + ' ' + car.brand + ' is deleted!', 'dark', 'bottom-right' );
    }
  }

  branchOfficeDeleted(branchOfficeId: number) {
    for (const branchOffice of this.carCompany.branchOffices) {
      if (branchOffice.id === branchOfficeId) {
        branchOffice.active = false;
        this.ngxNotificationService.sendMessage(branchOffice.name + ' is deleted!', 'dark', 'bottom-right' );
        break;
      }
    }
  }

  openNewCarModal() {
    const initialState = {
      carCompanyId: this.carCompany.id
    };
    this.modalRef = this.modalService.show(NewCarFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(car => {
      this.ngxNotificationService.sendMessage(car.brand + ' ' + car.model + ' created!', 'dark', 'bottom-right');
      this.carCompany.cars.push(car);
    });
  }

  openNewBranchOfficeModal() {
    const initialState = {
      carCompanyId: this.carCompany.id
    };
    this.modalRef = this.modalService.show(NewBranchOfficeFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(branchOffice => {
      this.carCompany.branchOffices.push(branchOffice);
      this.ngxNotificationService.sendMessage(branchOffice.name + ' created!', 'dark', 'bottom-right');
    });
  }
}
