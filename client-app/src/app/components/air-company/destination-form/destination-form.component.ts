import { HttpErrorResponse } from '@angular/common/http';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { Destination } from './../../../model/air-company/destination';
import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-destination-form',
  templateUrl: './destination-form.component.html',
  styleUrls: ['./destination-form.component.css', '../../../shared/css/inputField.css']
})
export class DestinationFormComponent implements OnInit {
  errorMessage: String = '';
  @Input() destination: Destination = new Destination();
  @Input() aircompanyId: Number;
  @Input() isAddForm: Boolean = false;

  public onClose: Subject<Destination>;
  destinationForm: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private airCompanyService: AirCompanyService ) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.destinationForm = this.formBuilder.group({
      label: [this.destination.label],
      country: [this.destination.country],
      airportName: [this.destination.airportName]
    });
  }

  onSubmit() {
    if (this.isAddForm) {
      this.addDestination();
    } else {
      this.editDestination();
    }
  }

  addDestination() {
    this.airCompanyService.addDestination(this.destinationForm.value, this.aircompanyId).subscribe(
      data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );
  }

  editDestination() {
    const value = this.destinationForm.value;
    value.id = this.destination.id;
    this.airCompanyService.editDestination(value, this.aircompanyId).subscribe(
      data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );
  }

}
