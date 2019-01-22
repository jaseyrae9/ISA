import { Component, OnInit, Output, ViewChild, ElementRef, EventEmitter, Input} from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-branch-office-form',
  templateUrl: './new-branch-office-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css', './new-branch-office-form.component.css']
})
export class NewBranchOfficeFormComponent implements OnInit {
  // @Output() branchOfficeCreated: EventEmitter<BranchOffice> = new EventEmitter();
  errorMessage: String = '';

  public onClose: Subject<BranchOffice>;
  branchOffice: BranchOffice = new BranchOffice();
  @Input() carCompanyId: Number;
  newBranchOfficeForm: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder,
    private route: ActivatedRoute, private rentACarCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.newBranchOfficeForm = this.formBuilder.group({
      name: [this.branchOffice.name, [Validators.required]],
      });
  }

  onBranchOfficeAdd() {
    this.rentACarCompanyService.addBranchOffice(this.newBranchOfficeForm.value, this.carCompanyId).subscribe(
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
