import { Component, OnInit, Input } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { Options, LabelType } from 'ng5-slider';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Room } from 'src/app/model/hotel/room';

@Component({
  selector: 'app-book-form',
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.css', '../../../shared/css/inputField.css']
})
export class BookFormComponent implements OnInit {
  @Input() rooms: Room[] = [];
  visibleRooms: Room[] = []; // sobe koje ce se prikazati posle search-a


  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  priceRange = [0, 200];

  bookForm: FormGroup;
  bsRangeValue: Date[];

  minValue = 0;
  maxValue = 200;

  options: Options = {
    floor: 0,
    ceil: 200,
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
          return '<b>Min price:</b> $' + value;
        case LabelType.High:
          return '<b>Max price:</b> $' + value;
        default:
          return '$' + value;
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
    console.log('heeeej');
    this.bookForm = this.formBuilder.group({
      numberOfGuests: ['', [Validators.min(0)]],
      type: ['Regular'],
      bsRangeValue: [this.bsRangeValue],
      priceRange: [this.priceRange]
    });
  }

  onSearch() {
    this.visibleRooms = [];
    console.log('izabrani tip ' + this.bookForm.value.type);

    for (const room of this.rooms) {
      console.log('naisli na tip ' + room.type);
      if (room.type === this.bookForm.value.type &&
        room.numberOfBeds >= this.bookForm.value.numberOfGuests) {
        console.log('room number ' + room.roomNumber + ' tip ' + room.type);
        if (room.price >= this.bookForm.value.priceRange[0] && room.price <= this.bookForm.value.priceRange[1]) {
          console.log('okej');
          this.visibleRooms.push(room);
          console.log(this.visibleRooms);
        }
      }
    }
  }

}
