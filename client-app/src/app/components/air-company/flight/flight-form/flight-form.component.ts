import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Destination } from './../../../../model/air-company/destination';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-flight-form',
  templateUrl: './flight-form.component.html',
  styleUrls: ['./flight-form.component.css', './../../../../shared/css/inputField.css']
})
export class FlightFormComponent implements OnInit {
  errorMessage: String = '';
  flightForm: FormGroup;
  @Input() airCompanyId;
  @Input() isAddForm = false;
  @Input() flight: Flight = new Flight();
  destinations: Destination[] = [];
  selectedDestinations: Destination[] = [];
  public onClose: Subject<Flight>;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private airCompanyService: AirCompanyService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.flightForm = this.formBuilder.group({
      destinationSelectBox: [0],
      selectedDestinations: [[]]
    });
    this.airCompanyService.get(this.airCompanyId).subscribe(
      (data) => {
        this.destinations = data.destinations;
      }
    );
  }

  addDestination() {
    const index = this.flightForm.value.destinationSelectBox;
    this.selectedDestinations.push(this.destinations[index]);
    this.flightForm.controls['selectedDestinations'].setValue(this.selectedDestinations);
  }

  submit() {
    if (this.isAddForm) {
      this.addFlight();
    } else {
      this.editFlight();
    }
  }

  addFlight() {

  }

  editFlight() {

  }
}
