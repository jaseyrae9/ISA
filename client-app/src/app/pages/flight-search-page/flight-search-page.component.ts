import { Component, OnInit } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { Options, LabelType } from 'ng5-slider';

@Component({
  selector: 'app-flight-search-page',
  templateUrl: './flight-search-page.component.html',
  styleUrls: ['./flight-search-page.component.css', '../../shared/css/inputField.css']
})
export class FlightSearchPageComponent implements OnInit {
  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  bsRangeValue: Date[];

  minCarryOnBags = 0;
  maxCarryOnBags = 4;

  minCheckInBaggage = 0;
  maxCheckInBaggage = 4;

  min = 0; // for stops
  max = 4;

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
          return '<b>Min carry on bags: </b>' + value;
        case LabelType.High:
          return '<b>Max carry on bags: </b>' + value;
        default:
          return 'Cary on bags: ' + value;
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
          return '<b>Min check in baggage: </b>' + value;
        case LabelType.High:
          return '<b>Max check in baggage: </b>' + value;
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
          return '<b>Min stops: </b>' + value;
        case LabelType.High:
          return '<b>Max stops: </b> ' + value + '+';
        default:
          return 'Stops: ' + value;
      }
    }
  };


  constructor() {
    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
  }

}
