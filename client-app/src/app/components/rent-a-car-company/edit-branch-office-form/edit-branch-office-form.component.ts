import { Component, OnInit, Input } from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Subject } from 'rxjs/Subject';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-edit-branch-office-form',
  templateUrl: './edit-branch-office-form.component.html',
  styleUrls: ['./edit-branch-office-form.component.css', '../../../shared/css/inputField.css']
})
export class EditBranchOfficeFormComponent implements OnInit {
  @Input() branchOffice: BranchOffice;
  @Input() companyId: string;
  public onClose: Subject<BranchOffice>;

  constructor(private carCompanyService: RentACarCompanyService, public modalRef: BsModalRef) { }

  ngOnInit() {
    this.onClose = new Subject();
  }

  onBranchOfficeEdit() {
    this.carCompanyService.editBranchOffice(this.branchOffice, this.companyId).subscribe(
      data => {
        this.onClose.next(this.branchOffice);
        this.modalRef.hide();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
