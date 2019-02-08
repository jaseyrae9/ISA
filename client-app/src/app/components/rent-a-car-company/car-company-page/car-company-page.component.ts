import { Component, OnInit, Input } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { AlertService } from 'ngx-alerts';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NewCarFormComponent } from '../new-car-form/new-car-form.component';
import { NewBranchOfficeFormComponent } from '../new-branch-office-form/new-branch-office-form.component';
import { EditCarCompanyFormComponent } from '../edit-car-company-form/edit-car-company-form.component';
import { formatDate } from '@angular/common';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { Car } from 'src/app/model/rent-a-car-company/car';

@Component({
  selector: 'app-car-company-page',
  templateUrl: './car-company-page.component.html',
  styleUrls: ['./car-company-page.component.css', '../../../shared/css/inputField.css']
})
export class CarCompanyPageComponent implements OnInit {
  fastCarRent: Car[] = [];

  modalRef: BsModalRef;
  carCompany: RentACarCompany = new RentACarCompany();
  companyId: number;
  maxValue = 0;

  monthlyCarReservation: any;
  weeklyCarReservation: any;
  dailyCarReservation: any;

  income: any;
  bsRangeValue: Date[];
  datePickerConfig: Partial<BsDatepickerConfig>;

  constructor(private modalService: BsModalService,
     private route: ActivatedRoute,
     private carService: RentACarCompanyService,
     public tokenService: TokenStorageService,
     private alertService: AlertService) {
      this.datePickerConfig = Object.assign({},
        {
          containerClass: 'theme-default',
          dateInputFormat: 'YYYY-MM-DD'
        });
      }

  ngOnInit() {
    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = +companyId; // + -> string u int

    this.getCompanie();
    this.getFastCars();

    if (this.tokenService.isCarAdmin && this.tokenService.companyId === this.companyId) {
      this.carService.getMonthlyResevations(companyId).subscribe(
        (data) => {
          console.log('Mesecne rezervacije: ', data);
          this.monthlyCarReservation = data;
        }
      );
    }
    if (this.tokenService.isCarAdmin && this.tokenService.companyId === this.companyId) {
      this.carService.getWeeklyResevations(companyId).subscribe(
        (data) => {
          console.log('Nedeljne rezervacije: ', data);
          this.weeklyCarReservation = data;
        }
      );
    }
    if (this.tokenService.isCarAdmin && this.tokenService.companyId === this.companyId) {
      this.carService.getDailyResevations(companyId).subscribe(
        (data) => {
          console.log('Dnevne rezervacije: ', data);
          this.dailyCarReservation = data;
        }
      );
    }
  }

  getCompanie() {
    this.carService.get(this.companyId).subscribe(
      (data) => {
        this.carCompany = data;
        console.log('Otvorena je kompanija: ', this.carCompany);
      }
    );
  }

  getFastCars() {
    this.carService.getFastCars(this.companyId).subscribe(
      (data) => {
        this.fastCarRent = data;
        console.log('Fast cars ', data);
      }
    );
  }
  // refresh when clicked fast button
  carFasten(carId) {
    const index: number = this.carCompany.cars.findIndex(e => e.id === carId);
    if (index !== -1) {
      this.carCompany.cars.splice(index, 1);
    }
    this.getFastCars();
  }

  onTicketDeleted(carId: number) {
    const index: number = this.fastCarRent.findIndex(e => e.id === carId);
    if (index !== -1) {
      this.fastCarRent.splice(index, 1);
      this.alertService.info('Car is not anymore in fast reservations!');
    }
    this.getCompanie();
  }
  carDeleted(carId: number) {
    const index: number = this.carCompany.cars.findIndex(e => e.id === carId);
    if (index !== -1) {
      this.carCompany.cars.splice(index, 1);
      this.alertService.info('Car is deleted!');
    }
  }

  branchOfficeDeleted(branchOfficeId: number) {
    const index: number = this.carCompany.branchOffices.findIndex(e => e.id === branchOfficeId);
    if (index !== -1) {
      this.carCompany.branchOffices.splice(index, 1);
      this.alertService.info('Branch office is deleted!');
    }
  }

  openNewCarModal() {
    const initialState = {
      carCompanyId: this.carCompany.id
    };
    this.modalRef = this.modalService.show(NewCarFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(car => {
      this.alertService.info(car.brand + ' ' + car.model + ' created!');
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
      this.alertService.info(branchOffice.name + ' created!');
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
       this.alertService.info('Rent a car company is changed!');
      });
   }

   getIncome() {
    const date0 = new Date(this.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    console.log(datum0);
    const date1 = new Date(this.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');
    console.log(datum1);

    this.carService.getIncome(this.carCompany.id, datum0, datum1).subscribe(
      data => {
        this.income = data;
      },
      error => {
        console.log(error.error.message);
      }
    );
   }
}
