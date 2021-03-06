import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { AlertService } from 'ngx-alerts';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { NewCarCompanyFormComponent } from 'src/app/components/rent-a-car-company/new-car-company-form/new-car-company-form.component';

@Component({
  selector: 'app-all-cars-companies-page',
  templateUrl: './all-cars-companies-page.component.html',
  styleUrls: ['./all-cars-companies-page.component.css', '../../shared/css/inputField.css']
})

export class AllCarsCompaniesPageComponent implements OnInit {
  sort  = 'name';
  modalRef: BsModalRef;
  companies: RentACarCompany[] = [];

  pages: Array<Number> = new Array();
  pageNumber = 0;

  constructor(private modalService: BsModalService,
    private rentACarCompanyService: RentACarCompanyService,
    private dataService: DataService,
    public tokenService: TokenStorageService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    this.loadCompanies();
  }

  loadCompanies() {
    this.rentACarCompanyService.getAll(this.pageNumber, this.sort).subscribe(data => {
      this.companies = data['content'];
      this.pages = new Array(data['totalPages']);
    });
  }

  onSortChange(value) {
    this.sort = value;
    this.loadCompanies();
  }

  openNewCarCompanyModal() {
    this.modalRef = this.modalService.show(NewCarCompanyFormComponent);
    this.modalRef.content.onClose.subscribe(carCompany => {
      this.alertService.info(carCompany.name + ' is created!');
      this.companies.push(carCompany);
      this.dataService.changeCarCompany(carCompany);
      this.loadCompanies();
    });
  }


  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i < this.pages.length) {
      this.pageNumber += i;
      this.loadCompanies();
    }
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.loadCompanies();
  }
}
