import { AirplaneFormComponent } from './../airplane/airplane-form/airplane-form.component';
import { NgxNotificationService } from 'ngx-notification';
import { Airplane } from './../../../model/air-company/airplane';
import { EditAirCompanyFormComponent } from './../edit-air-company-form/edit-air-company-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { DestinationFormComponent } from './../destination-form/destination-form.component';
import { Destination } from './../../../model/air-company/destination';
import { Component, OnInit } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';

@Component({
  selector: 'app-air-company-page',
  templateUrl: './air-company-page.component.html',
  styleUrls: ['./air-company-page.component.css', '../../../shared/css/inputField.css']
})
export class AirCompanyPageComponent implements OnInit {
  modalRef: BsModalRef;
  airCompany: AirCompany = new AirCompany();
  airplanes: Airplane[] = [];

  constructor(private ngxNotificationService: NgxNotificationService,
    private modalService: BsModalService, private route: ActivatedRoute, private airCompanyService: AirCompanyService,
    public tokenService: TokenStorageService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airCompanyService.get(id).subscribe(
      (data) => {
        this.airCompany = data;
      }
    );
    this.loadAirplanes(id);
    this.tokenService.rolesEmitter.subscribe(
      (data) => { if (data !== null) { this.loadAirplanes(this.airCompany.id); } }
    );
   }

   loadAirplanes(id) {
    console.log(this.tokenService.isAirAdmin);
    if (this.tokenService.isAirAdmin) {
      this.airCompanyService.getAllAirplanes(id).subscribe(
        (data) => {
          this.airplanes = data;
        }
      );
    }
   }

   openAddDestinationModal() {
    const initialState = {
      aircompanyId: this.airCompany.id,
      isAddForm: true
    };
    this.modalRef = this.modalService.show(DestinationFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(destination => {
      this.ngxNotificationService.sendMessage('Destination ' + destination.label + ' added.', 'dark', 'bottom-right');
      this.airCompany.destinations.push(destination);
    });
  }

  onDeleteDestination(destination: Destination) {
    const index: number = this.airCompany.destinations.indexOf(destination);
    if (index !== -1) {
      this.airCompany.destinations.splice(index, 1);
    }
  }

  openAddAirplaneModal() {
    const initialState = {
      aircompanyId: this.airCompany.id,
      isAddForm: true
    };
    this.modalRef = this.modalService.show(AirplaneFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(airplane => {
      this.ngxNotificationService.sendMessage('Airpane ' + airplane.name + ' added.', 'dark', 'bottom-right');
      this.airplanes.push(airplane);
    });
  }

  onDeleteAirplane(airplane: Airplane) {
    const index: number = this.airplanes.indexOf(airplane);
    if (index !== -1) {
      this.airplanes.splice(index, 1);
    }
  }

  openEditAirCompanyModal() {
    const initialState = {
      airCompany: this.airCompany
    };
    this.modalRef = this.modalService.show(EditAirCompanyFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(data => {
      this.ngxNotificationService.sendMessage('Company information edited.', 'dark', 'bottom-right');
      this.airCompany = data;
    });
  }
}
