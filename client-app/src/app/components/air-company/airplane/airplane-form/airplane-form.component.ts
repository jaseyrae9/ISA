import { HttpErrorResponse } from '@angular/common/http';
import { Subject } from 'rxjs/Subject';
import { Airplane } from 'src/app/model/air-company/airplane';
import { AirCompanyService } from './../../../../services/air-company/air-company.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Component, OnInit, Input } from '@angular/core';
import { Seat } from 'src/app/model/air-company/seat';

@Component({
  selector: 'app-airplane-form',
  templateUrl: './airplane-form.component.html',
  styleUrls: ['./airplane-form.component.css', '../../../../shared/css/inputField.css', '../../../../shared/css/seats.css']
})
export class AirplaneFormComponent implements OnInit {
  selectedClass = '';
  errorMessage: String = '';
  @Input() isAddForm: Boolean = false;
  @Input() airplane: Airplane = new Airplane();
  @Input() aircompanyId: Number;
  public onClose: Subject<Airplane>;
  form: FormGroup;
  seats: Seat[][] = [];
  public firstPage: Boolean = true;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private airCompanyService: AirCompanyService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.form = this.formBuilder.group({
      name: [this.airplane.name, [Validators.required]],
      colNum: [this.airplane.colNum, [Validators.required, Validators.min(1)]],
      rowNum: [this.airplane.rowNum, [Validators.required, Validators.min(1)]],
      seatsPerCol: [this.airplane.seatsPerCol, [Validators.required, Validators.min(1)]]
    });
    if (this.airplane.seats) {
        this.seats = JSON.parse(JSON.stringify(this.airplane.seats));
    }
  }

  onSubmit() {
    if (this.isAddForm) {
      this.addAirplane();
    } else {
      this.editAirplane();
    }
  }

  nextPage() {
    const value = this.form.value;
    const seats = [].concat.apply([], this.seats);
    if (seats.length !== value.seatsPerCol * value.rowNum * value.colNum) {
      this.seats = [];
      let row = 0;
      do {
        row += 1;
        const seatsInRow = [];
        let seatCounter = 0;
        do {
          const s = new Seat();
          s.seatClass = 'ECONOMY';
          s.rowNum = row;
          s.colNum = (seatCounter  / value.seatsPerCol) - (( seatCounter  % value.seatsPerCol ) / value.seatsPerCol) + 1;
          seatsInRow.push(s);
          seatCounter += 1;
        } while ( seatCounter < value.colNum * value.seatsPerCol);
        this.seats.push(seatsInRow);
      } while (row < value.rowNum );
    }
    this.firstPage = false;
  }

  addAirplane() {
    this.airCompanyService.addAirplane(this.aircompanyId, this.createAirplane()).subscribe( data => {
      this.onClose.next(data);
      this.modalRef.hide();
    },
    (err: HttpErrorResponse) => {
      // interceptor je hendlovao ove zahteve
      if (err.status === 401 || err.status === 403 || err.status === 404) {
        this.modalRef.hide();
      }
      this.firstPage = true;
      this.errorMessage = err.error.details;
    });
  }

  editAirplane() {
    const value = this.createAirplane();
    value.id = this.airplane.id;
    this.airCompanyService.editAirplane(this.aircompanyId, value).subscribe( data => {
      this.onClose.next(data);
      this.modalRef.hide();
    },
    (err: HttpErrorResponse) => {
      // interceptor je hendlovao ove zahteve
      if (err.status === 401 || err.status === 403 || err.status === 404) {
        this.modalRef.hide();
      }
      this.firstPage = true;
      this.errorMessage = err.error.details;
    });
  }

  createAirplane() {
    const value = this.form.value;
    value.seats = [].concat.apply([], this.seats);
    console.log(value.seats);
    return value;
  }

  onSeatChecked(event: any, i: number, j: number) {
    if (event.target.checked) {
      this.seats[i][j].seatClass = this.selectedClass;
    } else {
      this.seats[i][j].seatClass = 'ECONOMY';
    }
  }

}
