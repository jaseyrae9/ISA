import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { ViewChild, ElementRef } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService} from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-edit-car-company-form',
  templateUrl: './edit-car-company-form.component.html',
  styleUrls: ['./edit-car-company-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditCarCompanyFormComponent implements OnInit {
  errorMessage: String = '';
  public onClose: Subject<RentACarCompany>;
  @Input() carCompany: RentACarCompany;
  editCarCompanyForm: FormGroup;

  constructor (private formBuilder: FormBuilder, public modalRef: BsModalRef, private carCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editCarCompanyForm = this.formBuilder.group({
      id: [this.carCompany.id],
      name: [this.carCompany.name, [Validators.required]],
      description: [this.carCompany.description],
      });
  }

  onEditCarCompany() {
    this.carCompanyService.edit(this.editCarCompanyForm.value).subscribe(
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
