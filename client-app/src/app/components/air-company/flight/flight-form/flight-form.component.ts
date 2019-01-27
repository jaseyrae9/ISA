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
  flightForm: FormGroup;
  @Input() airCompanyId;
  @Input() isAddForm = false;
  @Input() flight: Flight = new Flight();

  // za izbor destinacija
  destinations: Destination[] = [];
  addedDestinations: Destination[] = [];
  selectedDestination = 0;

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
      }
    );
    this.airCompanyService.getActiveAirplanes(this.airCompanyId).subscribe(
      (data) => {
        this.airplanes = data;
      }
    );
    this.flightForm = this.formBuilder.group({
      economyPrice: [0, [Validators.required, Validators.min(0)]],
      premiumEconomyPrice: [0, [Validators.required, Validators.min(0)]],
      bussinessPrice: [0, [Validators.required, Validators.min(0)]],
      firstPrice: [0, [Validators.required, Validators.min(0)]],
      airplaneIndex: [0, [Validators.required]],
      length: [0, [Validators.required, Validators.min(0)]],
      maxCarryOnBags: [0, [Validators.required, Validators.min(0)]],
      maxCheckedBags: [0, [Validators.required, Validators.min(0)]],
      additionalServicesAvailable: [false],
      departureDate: [new Date(), Validators.required],
      arrivalDate: [new Date(), Validators.required],
      departureTime: ['00:00', Validators.required],
      arrivalTime: ['00:00', Validators.required]
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
    const value = this.flightForm.value;
    const flight = {
      airplaneId: this.airplanes[value.airplaneIndex].id,
      destinations: this.addedDestinations.map(function(a) {return a.id; }),
      startDateAndTime: this.datePipe.transform(value.departureDate, 'yyyy-MM-dd') + ' ' +  value.departureTime,
      endDateAndTime: this.datePipe.transform(value.arrivalDate, 'yyyy-MM-dd') + ' ' + value.arrivalTime,
      economyPrice: value.economyPrice,
      premiumEconomyPrice: value.premiumEconomyPrice,
      bussinessPrice: value.bussinessPrice,
      firstPrice: value.firstPrice,
      length: value.length,
      maxCarryOnBags: value.maxCarryOnBags,
      maxCheckedBags: value.maxCheckedBags,
      additionalServicesAvailable: value.additionalServicesAvailable
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

