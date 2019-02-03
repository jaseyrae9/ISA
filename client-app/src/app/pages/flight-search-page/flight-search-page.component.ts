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
  searchForm: FormGroup;
  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  bsRangeValue: Date[];

  minCarryOnBags = 0;
  minCheckInBaggage = 0;
  maxStops = 0;

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


  constructor(private formBuilder: FormBuilder) {
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

}
