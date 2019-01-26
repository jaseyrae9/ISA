import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AdditionalService } from 'src/app/model/additional-service';

@Component({
  selector: 'app-baggage-form',
  templateUrl: './baggage-form.component.html',
  styleUrls: ['./baggage-form.component.css', '../../../shared/css/inputField.css']
})
export class BaggageFormComponent implements OnInit {
  errorMessage: String = '';
  onClose: Subject<AdditionalService>;
  baggageForm: FormGroup;
  @Input() additionalService: AdditionalService = new AdditionalService();
  @Input() isAddForm = false;
  @Input() airCompanyId;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private airCompanyService: AirCompanyService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.baggageForm = this.formBuilder.group({
      name: [this.additionalService.name, [Validators.required]],
      description: [this.additionalService.description],
      price: [this.additionalService.price, [Validators.required, Validators.min(0)]],
      });
  }

  submit() {
    if (this.isAddForm) {
      this.addBaggageInfo();
    } else {
      this.editBaggageInfo();
    }
  }

  addBaggageInfo() {
    this.airCompanyService.addBaggageInformation(this.baggageForm.value, this.airCompanyId).subscribe(
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

  editBaggageInfo() {
    const value = this.baggageForm.value;
    value.id = this.additionalService.id;
    this.airCompanyService.editBaggageInformation(value, this.airCompanyId).subscribe(
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
