import { FlightFormComponent } from './../flight-form/flight-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { NgxNotificationService } from 'ngx-notification';
import { TouchSequence } from 'selenium-webdriver';

@Component({
  selector: 'app-flight-display',
  templateUrl: './flight-display.component.html',
  styleUrls: ['./flight-display.component.css']
})
export class FlightDisplayComponent implements OnInit {
  @Input() airCompanyId;
  flights: Flight[] = [];
  pages: Array<Number> = new Array();
  pageNumber = 0;
  modalRef: BsModalRef;

  constructor(private airService: AirCompanyService, public tokenService: TokenStorageService,
    private ngxNotificationService: NgxNotificationService, private modalService: BsModalService) { }

  ngOnInit() {
    this.loadFlights();
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
    const initialState = {
      airCompanyId: this.airCompanyId,
      isAddForm: true
    };
    this.modalRef = this.modalService.show(FlightFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(flight => {
      this.loadFlights();
      this.ngxNotificationService.sendMessage('Flight added.', 'dark', 'bottom-right');
    });
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
}
