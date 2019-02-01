import { Component, OnInit, Input } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { NgxNotificationService } from 'ngx-notification';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NewCarFormComponent } from '../new-car-form/new-car-form.component';
import { NewBranchOfficeFormComponent } from '../new-branch-office-form/new-branch-office-form.component';
import { EditCarCompanyFormComponent } from '../edit-car-company-form/edit-car-company-form.component';

@Component({
  selector: 'app-car-company-page',
  templateUrl: './car-company-page.component.html',
  styleUrls: ['./car-company-page.component.css', '../../../shared/css/inputField.css']
})
export class CarCompanyPageComponent implements OnInit {
  modalRef: BsModalRef;
  carCompany: RentACarCompany = new RentACarCompany();
  companyId: string;
  maxValue = 0;

  monthlyCarReservation: any;
  weeklyCarReservation: any;
  dailyCarReservation: any;

  income: any;

  constructor(private modalService: BsModalService,
     private route: ActivatedRoute,
     private carService: RentACarCompanyService,
     public tokenService: TokenStorageService,
     public ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = companyId;
    this.carService.get(companyId).subscribe(
      (data) => {
        this.carCompany = data;
        console.log('Otvorena je kompanija: ', this.carCompany);
      }
    );
    if (this.tokenService.isCarAdmin) {
      this.carService.getMonthlyResevations(companyId).subscribe(
        (data) => {
          console.log('Mesecne rezervacije: ', data);
          this.monthlyCarReservation = data;
        }
      );
    }
    if (this.tokenService.isCarAdmin) {
      this.carService.getWeeklyResevations(companyId).subscribe(
        (data) => {
          console.log('Nedeljne rezervacije: ', data);
          this.weeklyCarReservation = data;
        }
      );
    }
    if (this.tokenService.isCarAdmin) {
      this.carService.getDailyResevations(companyId).subscribe(
        (data) => {
          console.log('Dnevne rezervacije: ', data);
          this.dailyCarReservation = data;
        }
      );
    }
  }

  carDeleted(carId: number) {
    const index: number = this.carCompany.cars.findIndex(e => e.id === carId);
    if (index !== -1) {
      this.carCompany.cars.splice(index, 1);
      this.ngxNotificationService.sendMessage('Car is deleted!', 'dark', 'bottom-right');
    }
  }

  branchOfficeDeleted(branchOffice: BranchOffice) {
    const index: number = this.carCompany.branchOffices.indexOf(branchOffice);
    if (index !== -1) {
      this.carCompany.branchOffices.splice(index, 1);
      this.ngxNotificationService.sendMessage(branchOffice.name + ' is deleted!', 'dark', 'bottom-right' );
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

  // editing car company
  openEditModal() {
    const initialState = {
       carCompany: this.carCompany,
       companyId: this.companyId
     };
     this.modalRef = this.modalService.show(EditCarCompanyFormComponent, { initialState });
     this.modalRef.content.onClose.subscribe(carCompany => {
       this.carCompany = carCompany;
       this.ngxNotificationService.sendMessage('Rent a car company is changed!', 'dark', 'bottom-right' );
      });
   }
}
