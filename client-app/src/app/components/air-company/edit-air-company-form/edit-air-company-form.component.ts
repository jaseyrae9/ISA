import { Subject } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AirCompany } from './../../../model/air-company/air-company';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { ViewChild, ElementRef } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { NgModule } from '@angular/core';

@Component({
  selector: 'app-edit-air-company-form',
  templateUrl: './edit-air-company-form.component.html',
  styleUrls: ['./edit-air-company-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditAirCompanyFormComponent implements OnInit {
  public onClose: Subject<AirCompany>;
  @Input() airCompany: AirCompany;
  errorMessage: String = '';
  editForm: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private airCompanyService: AirCompanyService ) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editForm = this.formBuilder.group({
      name: [this.airCompany.name, [Validators.required]],
      description: [this.airCompany.description]
    });
  }

  onEditAirCompany() {
    const value = this.editForm.value;
    value.id = this.airCompany.id;
    this.airCompanyService.edit(value).subscribe(
      data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      error => {
        // interceptor je hendlovao ove zahteve
        if (error.status === 401 || error.status === 403 || error.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = error.error.details;
      }
    );
  }

}
