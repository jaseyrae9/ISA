import { EditAirCompanyFormComponent } from './../../components/air-company/edit-air-company-form/edit-air-company-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { DataService } from './../../observables/data.service';
import { Component, OnInit } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-all-air-companies-page',
  templateUrl: './all-air-companies-page.component.html',
  styleUrls: ['./all-air-companies-page.component.css', '../../shared/css/inputField.css']
})
export class AllAirCompaniesPageComponent implements OnInit {
  modalRef: BsModalRef;
  companies: AirCompany[];

  constructor(private airCompanyService: AirCompanyService,
     private dataService: DataService,
     public tokenService: TokenStorageService, private ngxNotificationService: NgxNotificationService,
     private modalService: BsModalService) {
   }

  ngOnInit() {
    this.airCompanyService.getAll().subscribe(data => {
      this.companies = data;
    });
  }

  openAddAirCompmanyModal() {
    const initialState = {
      isAddForm: true
    };
    this.modalRef = this.modalService.show(EditAirCompanyFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(data => {
      this.ngxNotificationService.sendMessage('Air company' + data.name + ' created.', 'dark', 'bottom-right');
      this.companies.push(data);
      this.dataService.changeAirCompany(data);
    });
  }
}
