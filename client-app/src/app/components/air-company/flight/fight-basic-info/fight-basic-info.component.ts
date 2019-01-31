import { FlightFormComponent } from './../flight-form/flight-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { NgxNotificationService } from 'ngx-notification';
import { BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-fight-basic-info',
  templateUrl: './fight-basic-info.component.html',
  styleUrls: ['./fight-basic-info.component.css', '../../../../shared/css/inputField.css', '../../../../shared/css/deleteAndEditLinks.css']
})
export class FightBasicInfoComponent implements OnInit {
  modalRef: BsModalRef;
  @Output() flightEvent: EventEmitter<Object> = new EventEmitter();
  @Input() flight = new Flight();
  constructor(private ngxNotificationService: NgxNotificationService, public tokenService: TokenStorageService,
     private airService: AirCompanyService, private modalService: BsModalService) { }

  ngOnInit() {
  }

  deleteFlight () {
    this.airService.deleteFlight(this.flight.airCompanyBasicInfo.id, this.flight.id).subscribe(
      () => {
        const data = {status: 0};
        this.flightEvent.emit(data);
      }
      // neuspesno brisanje ce handlovati interceptori
    );
  }

  activateFlight() {
    this.airService.activateFlight(this.flight.airCompanyBasicInfo.id, this.flight.id).subscribe(
      (data) => {
        this.ngxNotificationService.sendMessage('Flight activated.', 'dark', 'bottom-right');
        this.flight = data;
      }
      // neuspesnu aktivaciju ce handlovati interceptori
    );
  }

  openEditForm() {
    const initialState = {
      airCompanyId: this.flight.airCompanyBasicInfo.id,
      flight: this.flight,
      isAddForm: false
    };
    this.modalRef = this.modalService.show(FlightFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(fligth => {
      this.flight.airplane  = fligth.airplane;
      this.flight.destinations = fligth.destinations;
      this.flight.startDateAndTime = fligth.startDateAndTime;
      this.flight.endDateAndTime = fligth.endDateAndTime;
      this.flight.duration = fligth.duration;
      this.flight.length = fligth.length;
      this.flight.airCompanyBasicInfo = fligth.airCompanyBasicInfo;
      this.flight.maxCarryOnBags = fligth.maxCarryOnBags;
      this.flight.maxCheckedBags = fligth.maxCheckedBags;
      this.flight.additionalServicesAvailable = fligth.additionalServicesAvailable;
      this.flight.status = fligth.status;
      this.flight.economyPrice = fligth.economyPrice;
      this.flight.premiumEconomyPrice = fligth.premiumEconomyPrice;
      this.flight.firstPrice = fligth.firstPrice;
      this.flight.bussinessPrice = fligth.bussinessPrice;
      this.flight.tickets = fligth.tickets;
      this.ngxNotificationService.sendMessage('Flight edited.', 'dark', 'bottom-right');
    });
  }
}
