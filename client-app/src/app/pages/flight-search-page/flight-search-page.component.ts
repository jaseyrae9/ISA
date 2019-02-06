import { DatePipe } from '@angular/common';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Flight } from './../../model/air-company/flight';
import { Component, OnInit } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { Options, LabelType } from 'ng5-slider';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-flight-search-page',
  templateUrl: './flight-search-page.component.html',
  styleUrls: ['./flight-search-page.component.css', '../../shared/css/inputField.css']
})
export class FlightSearchPageComponent implements OnInit {
  isSearchActive = false;
  flights: Flight[] = [];

  isFilterActive = false;
  filteredFlights: Flight[] = [];

  searchForm: FormGroup;
  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  bsRangeValue: Date[];

  minCarryOnBags = 0;
  minCheckInBaggage = 0;
  maxStops = 0;
  additionalServicesNeeded = false;

  optionsCarryOnBags: Options = {
    floor: 0,
    ceil: 4,
    showSelectionBar: true,
    selectionBarGradient: {
      from: 'white',
      to: '#33cabb'
    },

    getPointerColor: (value: number): string => {
      return '#33cabb';
    },

    translate: (value: number, label: LabelType): string => {
      switch (label) {
      case LabelType.Low:
        return '<b>Carry on bags: </b>' + value + '+';
      default:
        return 'Carry on bags: ' + value;
      }
    }
  };

  optionsCheckInBaggage: Options = {
    floor: 0,
    ceil: 4,
    showSelectionBar: true,
    selectionBarGradient: {
      from: 'white',
      to: '#33cabb'
    },

    getPointerColor: (value: number): string => {
      return '#33cabb';
    },

    translate: (value: number, label: LabelType): string => {
      switch (label) {
        case LabelType.Low:
          return '<b>Min check in baggage: </b>' + value + '+';
        default:
          return 'Check in baggage: ' + value;
      }
    }
  };

  optionsNumber: Options = {
    floor: 0,
    ceil: 4,
    showSelectionBar: true,
    selectionBarGradient: {
      from: 'white',
      to: '#33cabb'
    },

    getPointerColor: (value: number): string => {
      return '#33cabb';
    },

    translate: (value: number, label: LabelType): string => {
      switch (label) {
        case LabelType.Low:
          return '<b>Max stops: </b>' + value + '+';
        default:
          return 'Stops: ' + value;
      }
    }
  };


  constructor(private formBuilder: FormBuilder, private airCompanyService: AirCompanyService, public datePipe: DatePipe) {
    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
    this.searchForm = this.formBuilder.group({
      departureAirport: ['', [Validators.required]],
      arrivalAirport: ['', [Validators.required]],
      dates: ['', [Validators.required]],
      numberOfPeople: ['', [Validators.required, Validators.min(1)]],
      minPrice: ['', [Validators.min(0)]],
      maxPrice: ['', [Validators.min(0)]]
    });
  }

  cancelSearch() {
    this.isSearchActive = false;
    this.filteredFlights = [];
    this.flights = [];
  }

  cancelFilter() {
    this.isFilterActive = false;
    this.filteredFlights = this.flights;
  }

  search() {
    const value = this.searchForm.value;
    const searchData = {
      departureAirport: value.departureAirport,
      arrivalAirport: value.arrivalAirport,
      start: this.datePipe.transform(value.dates[0], 'yyyy-MM-dd'),
      end: this.datePipe.transform(value.dates[1], 'yyyy-MM-dd'),
      numberOfPeople: value.numberOfPeople,
      maxPrice: value.maxPrice,
      minPrice: value.minPrice
    };
    this.airCompanyService.searchFlights(searchData).subscribe(
      (data) => {
        this.isSearchActive = true;
        this.isFilterActive = false;
        this.flights = data;
        this.filteredFlights = data;
      },
      (error) => {
        this.errorMessage = error.error.details;
      }
    );
  }

  filter() {
    this.isFilterActive = true;
    this.filteredFlights = this.flights.filter(flight => this.matchFilter(flight) );
  }

  matchFilter(flight: Flight) {
    if ( flight.maxCarryOnBags < this.minCarryOnBags ) {
      return false;
    }
    if (flight.maxCheckedBags < this.minCheckInBaggage) {
      return false;
    }
    if (flight.destinations.length - 2 > this.maxStops) {
      return false;
    }
    if (this.additionalServicesNeeded && !flight.additionalServicesAvailable) {
      return false;
    }
    return true;
  }
}
