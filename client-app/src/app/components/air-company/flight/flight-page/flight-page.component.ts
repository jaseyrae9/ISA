import { DisableSeatsFormComponent } from './../disable-seats-form/disable-seats-form.component';
import { CreateFastReservationsFormComponent } from './../create-fast-reservations-form/create-fast-reservations-form.component';
import { ChangeTicketsPricesFormComponent } from './../change-tickets-prices-form/change-tickets-prices-form.component';
import { FlightFormComponent } from './../flight-form/flight-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AirCompanyService } from './../../../../services/air-company/air-company.service';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit} from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { AlertService } from 'ngx-alerts';
import { BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-flight-page',
  templateUrl: './flight-page.component.html',
  styleUrls: ['./flight-page.component.css', '../../../../shared/css/inputField.css', '../../../../shared/css/deleteAndEditLinks.css']
})
export class FlightPageComponent implements OnInit {
  modalRef: BsModalRef;
  flight: Flight = new Flight();
  constructor(private alertService: AlertService, private router: Router, private airService: AirCompanyService ,
    public tokenService: TokenStorageService, private route: ActivatedRoute, private modalService: BsModalService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airService.getFlight(id).subscribe(
      (data) => {
        this.flight = data;
      }
    );
  }

  deleteFlight () {
    this.airService.deleteFlight(this.flight.airCompanyBasicInfo.id, this.flight.id).subscribe(
      (data) => {
        this.router.navigate(['/aircompany', this.flight.airCompanyBasicInfo.id]);
      }
      // neuspesno brisanje ce handlovati interceptori
    );
  }

  activateFlight() {
    this.airService.activateFlight(this.flight.airCompanyBasicInfo.id, this.flight.id).subscribe(
      (data) => {
        this.alertService.info('Flight activated.');
        this.flight.status = data.status;
      }
      // neuspesnu aktivaciju ce handlovati interceptori
    );
  }

  openChangePricesForm() {
    const initialState = {
      flight: this.flight
    };
    this.modalRef = this.modalService.show(ChangeTicketsPricesFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(fligth => {
      this.setFlight(fligth);
      this.alertService.info('Tickets prices changed.');
    });
  }

  openEditForm() {
    const initialState = {
      airCompanyId: this.flight.airCompanyBasicInfo.id,
      flight: this.flight,
      isAddForm: false
    };
    this.modalRef = this.modalService.show(FlightFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(fligth => {
      this.setFlight(fligth);
      this.alertService.info('Flight edited.');
    });
  }

  openDisableSeatsForm() {
    const initialState = {
      flight: this.flight
    };
    this.modalRef = this.modalService.show(DisableSeatsFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(fligth => {
      this.setFlight(fligth);
      this.alertService.info('Seats marked as unavailable.');
    });
  }

  openCreateFastTicketsForm() {
    const initialState = {
      flight: this.flight
    };
    this.modalRef = this.modalService.show(CreateFastReservationsFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(fligth => {
      this.setFlight(fligth);
      this.alertService.info('Fast reservations created.');
    });
  }

  setFlight(fligth) {
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
    this.flight.minPrice = fligth.minPrice;
  }

}
