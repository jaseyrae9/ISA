import { DatePipe } from '@angular/common';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { TicketForFastReservation } from './../../../model/air-company/ticket-for-fast-reservation';
import { AdditionalService } from 'src/app/model/additional-service';
import { AirplaneFormComponent } from './../airplane/airplane-form/airplane-form.component';
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
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { AlertService } from 'ngx-alerts';

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

  monthly: any;
  weekly: any;
  daily: any;

  form: FormGroup;
  datePickerConfig: Partial<BsDatepickerConfig>;
  income;

  constructor(private alertService: AlertService,
    private modalService: BsModalService, private route: ActivatedRoute, private airCompanyService: AirCompanyService,
    public tokenService: TokenStorageService, private formBuilder: FormBuilder, private datePipe: DatePipe) {
      this.datePickerConfig = Object.assign({},
        {
          containerClass: 'theme-default',
          dateInputFormat: 'YYYY-MM-DD'
        });

     }

  ngOnInit() {
    this.form = this.formBuilder.group({
      dates: ['', [Validators.required]]
    });
    this.id = this.route.snapshot.paramMap.get('id');
    this.airCompanyService.get(this.id).subscribe(
      (data) => {
        this.airCompany = data;
        console.log(this.airCompany);
      }
    );
    this.loadAirplanes(this.id);
    this.loadAdminInfo(this.id);
    this.tokenService.rolesEmitter.subscribe(
      (data) => { if (data !== null) { this.loadAirplanes(this.airCompany.id); this.loadAdminInfo(this.airCompany.id); } }
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
    // tslint:disable-next-line:triple-equals
    if (this.tokenService.isAirAdmin && this.tokenService.companyId == id) {
      this.airCompanyService.getAllAirplanes(id).subscribe(
        (data) => {
          this.airplanes = data;
        }
      );
    }
   }

   loadAdminInfo(id) {
      // tslint:disable-next-line:triple-equals
      if (this.tokenService.isAirAdmin && this.tokenService.companyId == id) {
        this.airCompanyService.getMonthly(id).subscribe(
          (data) => {
            this.monthly = data;
          }
        );
      }
      // tslint:disable-next-line:triple-equals
      if (this.tokenService.isAirAdmin && this.tokenService.companyId == id) {
        this.airCompanyService.getDaily(id).subscribe(
          (data) => {
            this.daily = data;
          }
        );
      }
      // tslint:disable-next-line:triple-equals
      if (this.tokenService.isAirAdmin && this.tokenService.companyId == id) {
        this.airCompanyService.getWeekly(id).subscribe(
          (data) => {
            this.weekly = data;
          }
        );
      }
   }

   getIncome() {
    const dates = this.form.value.dates;
    const from = this.datePipe.transform(dates[0], 'MM-dd-yyyy');
    const to = this.datePipe.transform(dates[1], 'MM-dd-yyyy');
    this.airCompanyService.getProfit(this.airCompany.id, from, to ).subscribe(
      (data) => {
        this.income = data;
      }
    );
   }

   openAddDestinationModal() {
    const initialState = {
      aircompanyId: this.airCompany.id,
      isAddForm: true
    };
    this.modalRef = this.modalService.show(DestinationFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(destination => {
      this.alertService.info('Destination ' + destination.label + ' added.');
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
      this.alertService.info('Airpane ' + airplane.name + ' added.');
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
      this.alertService.info('Company information edited.');
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
       this.alertService.info('Baggage ' + baggage.name + ' created!');
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
       this.alertService.info('Baggage ' + baggage.name + ' edited!');
    });
  }

  deleteBaggage(as: AdditionalService) {
    this.airCompanyService.deleteBaggageInformation(as.id, this.airCompany.id).subscribe(
      () => {
        const i = this.airCompany.baggageInformation.findIndex(e => e.id === as.id);
        if (i !== -1) {
          this.airCompany.baggageInformation.splice(i, 1);
        }
        this.alertService.info('Baggage information ' + as.name + ' deleted!');
      },
      error => {
        console.log(error.error.message);
      }
    );
  }
}
