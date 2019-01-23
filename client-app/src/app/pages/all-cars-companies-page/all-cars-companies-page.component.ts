import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { NgxNotificationService } from 'ngx-notification';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NewCarCompanyFormComponent } from 'src/app/components/rent-a-car-company/new-car-company-form/new-car-company-form.component';

@Component({
  selector: 'app-all-cars-companies-page',
  templateUrl: './all-cars-companies-page.component.html',
  styleUrls: ['./all-cars-companies-page.component.css', '../../shared/css/inputField.css']
})

export class AllCarsCompaniesPageComponent implements OnInit {
  modalRef: BsModalRef;
  companies: RentACarCompany[];

  constructor(private modalService: BsModalService, private rentACarCompanyService: RentACarCompanyService,
     private dataService: DataService, public tokenService: TokenStorageService, public ngxNotificationService: NgxNotificationService) {
  }

  ngOnInit() {
    this.rentACarCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
  }

  openNewCarCompanyModal() {
    this.modalRef = this.modalService.show(NewCarCompanyFormComponent);
      this.modalRef.content.onClose.subscribe(carCompany => {
        this.ngxNotificationService.sendMessage(carCompany.name + ' is created!', 'dark', 'bottom-right' );
        this.companies.push(carCompany);
        this.dataService.changeCarCompany(carCompany);
    });
  }
}
