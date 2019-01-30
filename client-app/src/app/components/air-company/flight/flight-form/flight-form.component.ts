import { HttpErrorResponse } from '@angular/common/http';
import { Airplane } from 'src/app/model/air-company/airplane';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Destination } from './../../../../model/air-company/destination';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-flight-form',
  templateUrl: './flight-form.component.html',
  styleUrls: ['./flight-form.component.css', './../../../../shared/css/inputField.css', '../../../../shared/css/deleteAndEditLinks.css']
})
export class FlightFormComponent implements OnInit {
  datePickerConfig: Partial<BsDatepickerConfig>;
  pageNumber = 0;
  errorMessage: String = '';
  flightForm1: FormGroup; // za poletanje sletanje itd
  flightForm2: FormGroup; // za cene
  @Input() airCompanyId;
  @Input() isAddForm = false;
  @Input() flight: Flight = new Flight();

  // za izbor destinacija
  destinations: Destination[] = [];
  addedDestinations: Destination[] = [];
  selectedDestination = 0;
  destinationWarning = '';

  // za izbor aviona
  airplanes: Airplane[] = [];

  public onClose: Subject<Flight>;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private airCompanyService: AirCompanyService,
    private datePipe: DatePipe) { }

  ngOnInit() {

    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
    });
    this.onClose = new Subject();
    this.airCompanyService.get(this.airCompanyId).subscribe(
      (data) => {
        this.destinations = data.destinations;
        for (const fd of this.flight.destinations) {
          this.addedDestinations.push(fd.destination);
        }
      }
    );
    this.airCompanyService.getActiveAirplanes(this.airCompanyId).subscribe(
      (data) => {
        this.airplanes = data;
      }
    );
    this.flightForm1 = this.formBuilder.group({
      length: [this.flight.length, [Validators.required, Validators.min(0)]],
      maxCarryOnBags: [this.flight.maxCarryOnBags, [Validators.required, Validators.min(0)]],
      maxCheckedBags: [this.flight.maxCheckedBags, [Validators.required, Validators.min(0)]],
      additionalServicesAvailable: [this.flight.additionalServicesAvailable],
      departureDate: [this.datePipe.transform(this.flight.startDateAndTime, 'yyyy-MM-dd'), Validators.required],
      arrivalDate: [this.datePipe.transform(this.flight.endDateAndTime, 'yyyy-MM-dd'), Validators.required],
      departureTime: [this.datePipe.transform(this.flight.endDateAndTime, 'hh:mm'), Validators.required],
      arrivalTime: [this.datePipe.transform(this.flight.endDateAndTime, 'hh:mm'), Validators.required]
    });
    this.flightForm2 = this.formBuilder.group({
      economyPrice: [this.flight.economyPrice, [Validators.required, Validators.min(0)]],
      premiumEconomyPrice: [this.flight.premiumEconomyPrice, [Validators.required, Validators.min(0)]],
      bussinessPrice: [this.flight.bussinessPrice, [Validators.required, Validators.min(0)]],
      firstPrice: [this.flight.firstPrice, [Validators.required, Validators.min(0)]],
      airplaneIndex: [0, [Validators.required]]
    });
  }

  addDestination() {
    const copy = Object.assign({}, this.destinations[this.selectedDestination]);
    this.addedDestinations.push( copy );
    if (this.addedDestinations.length >= 2) {
      this.errorMessage = '';
    }
  }

  removeDestination(destination) {
    const index = this.addedDestinations.indexOf(destination);
    this.addedDestinations.splice(index, 1);
    if (this.addedDestinations.length < 2) {
      this.errorMessage = 'Please, add at least two destinations.';
    }
  }

  submit() {
    if (this.isAddForm) {
      this.addFlight();
    } else {
      this.editFlight();
    }
  }

  addFlight() {
    this.airCompanyService.addFlight(this.airCompanyId, this.convertFormToDTO()).subscribe(
      (data) => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.pageNumber = 0;
        this.errorMessage = err.error.details;
      }
    );
  }

  editFlight() {

  }

  convertFormToDTO(): any {
    const value1 = this.flightForm1.value;
    const value2 = this.flightForm2.value;
    const flight = {
      airplaneId: this.airplanes[value2.airplaneIndex].id,
      destinations: this.addedDestinations.map(function(a) {return a.id; }),
      startDateAndTime: this.datePipe.transform(value1.departureDate, 'yyyy-MM-dd') + ' ' +  value1.departureTime,
      endDateAndTime: this.datePipe.transform(value1.arrivalDate, 'yyyy-MM-dd') + ' ' + value1.arrivalTime,
      economyPrice: value2.economyPrice,
      premiumEconomyPrice: value2.premiumEconomyPrice,
      bussinessPrice: value2.bussinessPrice,
      firstPrice: value2.firstPrice,
      length: value1.length,
      maxCarryOnBags: value1.maxCarryOnBags,
      maxCheckedBags: value1.maxCheckedBags,
      additionalServicesAvailable: value1.additionalServicesAvailable
    };
    return flight;
  }

  nextPage(i: number) {
    if (this.pageNumber === 0 && this.addedDestinations.length < 2) {
      this.errorMessage = 'Please, add at least two destinations.';
    } else {
      this.errorMessage = '';
      this.pageNumber += i;
    }
  }
}

