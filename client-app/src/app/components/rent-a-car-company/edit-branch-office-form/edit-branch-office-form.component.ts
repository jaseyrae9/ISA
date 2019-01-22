import { Component, OnInit, Input } from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Subject } from 'rxjs/Subject';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-edit-branch-office-form',
  templateUrl: './edit-branch-office-form.component.html',
  styleUrls: ['./edit-branch-office-form.component.css', '../../../shared/css/inputField.css']
})
export class EditBranchOfficeFormComponent implements OnInit {
  errorMessage: String = '';
  @Input() branchOffice: BranchOffice;
  @Input() companyId: string;
  public onClose: Subject<BranchOffice>;
  editBranchOfficeForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private carCompanyService: RentACarCompanyService, public modalRef: BsModalRef) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editBranchOfficeForm = this.formBuilder.group({
      id: [this.branchOffice.id],
      name: [this.branchOffice.name, [Validators.required]],
    });
  }

  onEditBranchOffice() {
    this.carCompanyService.editBranchOffice(this.editBranchOfficeForm.value, this.companyId).subscribe(
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
