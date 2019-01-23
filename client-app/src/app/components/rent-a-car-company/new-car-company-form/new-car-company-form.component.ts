import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService} from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-car-company-form',
  templateUrl: './new-car-company-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class NewCarCompanyFormComponent implements OnInit {
  errorMessage: String = '';

  carCompany: RentACarCompany = new RentACarCompany();
  public onClose: Subject<RentACarCompany>;
  newCarCompanyForm: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private rentACarCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.newCarCompanyForm = this.formBuilder.group({
      name: [this.carCompany.name, [Validators.required]],
      description: [this.carCompany.description],
      });
  }

  onCarCompanyAdd() {
  this.rentACarCompanyService.add(this.newCarCompanyForm.value).subscribe(
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
