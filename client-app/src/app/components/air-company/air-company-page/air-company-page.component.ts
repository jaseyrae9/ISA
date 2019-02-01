import { TicketForFastReservation } from './../../../model/air-company/ticket-for-fast-reservation';
import { AdditionalService } from 'src/app/model/additional-service';
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
import { BaggageFormComponent } from '../baggage-form/baggage-form.component';

@Component({
  selector: 'app-air-company-page',
  templateUrl: './air-company-page.component.html',
  styleUrls: ['./air-company-page.component.css', '../../../shared/css/inputField.css']
})
export class AirCompanyPageComponent implements OnInit {
  modalRef: BsModalRef;
  id;
  airCompany: AirCompany = new AirCompany();
  airplanes: Airplane[] = [];
  fastResTickets: TicketForFastReservation[] = [];

  constructor(private ngxNotificationService: NgxNotificationService,
    private modalService: BsModalService, private route: ActivatedRoute, private airCompanyService: AirCompanyService,
    public tokenService: TokenStorageService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.airCompanyService.get(this.id).subscribe(
      (data) => {
        this.airCompany = data;
      }
    );
    this.loadAirplanes(this.id);
    this.tokenService.rolesEmitter.subscribe(
      (data) => { if (data !== null) { this.loadAirplanes(this.airCompany.id); } }
    );
    this.loadFastReservationTickets();
   }

   loadFastReservationTickets() {
    this.airCompanyService.getTicketsForFastReservation(this.id).subscribe(
      (data) => {
        this.fastResTickets = data;
      }
    );
   }

   onTicketAdded() {
      this.loadFastReservationTickets();
   }

   onTicketDeleted(ticket) {
    console.log(ticket);
    const index: number = this.fastResTickets.indexOf(ticket);
    console.log(index);
    if (index !== -1) {
      this.fastResTickets.splice(index, 1);
    }
   }

   loadAirplanes(id) {
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
      this.airCompany.name = data.name;
      this.airCompany.description = data.description;
      this.airCompany.location = data.location;
    });
  }

  openBaggageModal() {
    const initialState = {
      airCompanyId: this.airCompany.id,
      isAddForm: true
    };
    this.modalRef = this.modalService.show(BaggageFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(baggage => {
       this.airCompany.baggageInformation.push(baggage);
       this.ngxNotificationService.sendMessage('Baggage ' + baggage.name + ' created!', 'dark', 'bottom-right');
    });
  }

  editBaggage(as: AdditionalService) {
    const i = this.airCompany.baggageInformation.findIndex(e => e.id === as.id);
    const initialState = {
      airCompanyId: this.airCompany.id,
      additionalService: this.airCompany.baggageInformation[i]
    };
    this.modalRef = this.modalService.show(BaggageFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(baggage => {
       this.airCompany.baggageInformation[i] = baggage;
       this.ngxNotificationService.sendMessage('Baggage ' + baggage.name + ' edited!', 'dark', 'bottom-right');
    });
  }

  deleteBaggage(as: AdditionalService) {
    this.airCompanyService.deleteBaggageInformation(as.id, this.airCompany.id).subscribe(
      () => {
        const i = this.airCompany.baggageInformation.findIndex(e => e.id === as.id);
        if (i !== -1) {
          this.airCompany.baggageInformation.splice(i, 1);
        }
        this.ngxNotificationService.sendMessage('Baggage information ' + as.name + ' deleted!', 'dark', 'bottom-right');
      },
      error => {
        console.log(error.error.message);
      }
    );
  }
}
