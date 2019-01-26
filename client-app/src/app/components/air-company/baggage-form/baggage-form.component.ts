import { Component, OnInit } from '@angular/core';
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
  public onClose: Subject<AdditionalService>;
  baggageForm: FormGroup;
  additionalService: AdditionalService = new AdditionalService();

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.baggageForm = this.formBuilder.group({
      name: [this.additionalService.name, [Validators.required]],
      description: [this.additionalService.description],
      price: [this.additionalService.price, [Validators.required, Validators.min(0)]],
      });
  }

}
