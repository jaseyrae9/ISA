import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { DestinationFormComponent } from './../destination-form/destination-form.component';
import { Destination } from './../../../model/air-company/destination';
import { Component, OnInit } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { ActivatedRoute } from '@angular/router';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';

@Component({
  selector: 'app-air-company-page',
  templateUrl: './air-company-page.component.html',
  styleUrls: ['./air-company-page.component.css', '../../../shared/css/inputField.css']
})
export class AirCompanyPageComponent implements OnInit {
  modalRef: BsModalRef;
  airCompany: AirCompany = new AirCompany();
  forEditing: AirCompany = new AirCompany();

  constructor(private modalService: BsModalService, private route: ActivatedRoute, private airCompanyService: AirCompanyService,
    public tokenService: TokenStorageService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airCompanyService.get(id).subscribe(
      (data) => {
        this.airCompany = data;
        this.forEditing = new AirCompany(data.id, data.name, data.description);
      }
    );
   }

  airCompanyEdited(data) {
    this.airCompany.id = data.id;
    this.airCompany.name = data.name;
    this.airCompany.description = data.description;
    this.forEditing = new AirCompany(data.id, data.name, data.description);
  }

  openAddDestinationModal() {
    const initialState = {
      aircompanyId: this.airCompany.id,
      isAddForm: true
    };
    this.modalRef = this.modalService.show(DestinationFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(destination => {
      this.airCompany.destinations.push(destination);
    });
  }

  onDeleteDestination(destination: Destination) {
    const index: number = this.airCompany.destinations.indexOf(destination);
    if (index !== -1) {
      this.airCompany.destinations.splice(index, 1);
    }
  }
}
