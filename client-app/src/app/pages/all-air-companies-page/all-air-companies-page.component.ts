import { EditAirCompanyFormComponent } from './../../components/air-company/edit-air-company-form/edit-air-company-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { AlertService } from 'ngx-alerts';

@Component({
  selector: 'app-all-air-companies-page',
  templateUrl: './all-air-companies-page.component.html',
  styleUrls: ['./all-air-companies-page.component.css', '../../shared/css/inputField.css']
})
export class AllAirCompaniesPageComponent implements OnInit {
  modalRef: BsModalRef;
  companies: AirCompany[] = [];

  pages: Array<Number> = new Array();
  pageNumber = 0;
  sort  = 'name';

  constructor(private airCompanyService: AirCompanyService,
     private dataService: DataService,
     public tokenService: TokenStorageService,
     private alertService: AlertService,
     private modalService: BsModalService) {
   }

  ngOnInit() {
    this.loadCompanies();
  }

  loadCompanies() {
    this.airCompanyService.getAllCompanies(this.pageNumber, this.sort).subscribe(data => {
      this.companies = data['content'];
      this.pages = new Array(data['totalPages']);
    });
  }

  openAddAirCompmanyModal() {
    const initialState = {
      isAddForm: true
    };
    this.modalRef = this.modalService.show(EditAirCompanyFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(data => {
      this.alertService.info('Air company' + data.name + ' created.');
      this.companies.push(data);
      this.dataService.changeAirCompany(data);
      this.loadCompanies();
    });
  }

  onSortChange(value) {
    this.sort = value;
    this.loadCompanies();
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
