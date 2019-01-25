import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditBranchOfficeFormComponent } from '../edit-branch-office-form/edit-branch-office-form.component';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-branch-office-basic-details',
  templateUrl: './branch-office-basic-details.component.html',
  styleUrls: ['./branch-office-basic-details.component.css', '../../../shared/css/inputField.css']
})

export class BranchOfficeBasicDetailsComponent implements OnInit {
  @Input() branchOffice: BranchOffice;
  @Output() branchOfficeDeleted: EventEmitter<BranchOffice> = new EventEmitter();

  carCompany: RentACarCompany = new RentACarCompany();

  modalRef: BsModalRef;
  companyId: string;

  constructor(private route: ActivatedRoute, private carCompanyService: RentACarCompanyService,
     public tokenService: TokenStorageService, private modalService: BsModalService,
     public ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = companyId;
  }

  deleteBranchOffice() {
    this.carCompanyService.deleteBranchOffice(this.branchOffice.id, this.companyId).subscribe(
      data => {
        console.log('deleteBranchOffice', this.branchOffice);
        this.branchOfficeDeleted.emit(this.branchOffice);
      },
      error => {
        console.log(error);
      }
    );
  }

  openEditModal() {
    const initialState = {
      branchOffice: this.branchOffice,
      companyId: this.companyId
    };
    this.modalRef = this.modalService.show(EditBranchOfficeFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(branchOffice => {
      this.branchOffice = branchOffice;
      this.ngxNotificationService.sendMessage('Branch office is changed!', 'dark', 'bottom-right' );
    });
  }
}
