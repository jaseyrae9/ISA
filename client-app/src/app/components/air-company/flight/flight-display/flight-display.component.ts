import { FlightFormComponent } from './../flight-form/flight-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { AlertService } from 'ngx-alerts';

@Component({
  selector: 'app-flight-display',
  templateUrl: './flight-display.component.html',
  styleUrls: ['./flight-display.component.css']
})
export class FlightDisplayComponent implements OnInit {
  @Input() airCompanyId;
  @Output() ticketsCreated: EventEmitter<Object> = new EventEmitter();
  flights: Flight[] = [];
  pages: Array<Number> = new Array();
  pageNumber = 0;
  modalRef: BsModalRef;

  constructor(private airService: AirCompanyService, public tokenService: TokenStorageService,
    private alertService: AlertService, private modalService: BsModalService) { }

  ngOnInit() {
    this.loadFlights();
    this.tokenService.rolesEmitter.subscribe(
      (data) => { if (data !== null) { this.loadFlights(); } }
    );
  }

  loadFlights() {
    this.airService.getFlights(this.airCompanyId, this.pageNumber).subscribe(
      (data) => {
        this.flights = data['content'];
        this.pages = new Array(data['totalPages']);
      }
    );
  }


  openAddForm() {
    this.airService.getActiveAirplanes(this.airCompanyId).subscribe(
      (data) => {
        if (data.length === 0) {
          alert('There are no active airplane. Flights can not be created');
        } else {
          const initialState = {
            airCompanyId: this.airCompanyId,
            isAddForm: true
          };
          this.modalRef = this.modalService.show(FlightFormComponent, { initialState });
          this.modalRef.content.onClose.subscribe(flight => {
            this.loadFlights();
            this.alertService.info('Flight added.');
          });
        }
      }
    );
  }

  arrowAction(i: number, event: any) {
    if ( this.pageNumber + i >= 0 && this.pageNumber + i < this.pages.length) {
      this.pageNumber += i;
      this.loadFlights();
    }
  }

  changePage(i: number, event: any) {
    this.pageNumber = i;
    this.loadFlights();
  }

  flightEvent(data) {
    this.loadFlights();
    if (data.status = 0) {
      this.alertService.info('Flight deleted.');
    } else {
      this.alertService.info('Flight edited.');
    }
  }

  ticketsCreatedEvent(data) {
    this.ticketsCreated.emit(data);
  }

}
