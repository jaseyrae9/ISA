import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditBranchOfficeFormComponent } from '../edit-branch-office-form/edit-branch-office-form.component';
import { AlertService } from 'ngx-alerts';

@Component({
  selector: 'app-branch-office-basic-details',
  templateUrl: './branch-office-basic-details.component.html',
  styleUrls: ['./branch-office-basic-details.component.css', '../../../shared/css/inputField.css']
})

export class BranchOfficeBasicDetailsComponent implements OnInit {
  @Input() branchOffice: BranchOffice;
  @Output() branchOfficeDeleted: EventEmitter<number> = new EventEmitter();

  carCompany: RentACarCompany = new RentACarCompany();

  modalRef: BsModalRef;
  companyId: number;

  constructor(private route: ActivatedRoute, private carCompanyService: RentACarCompanyService,
     public tokenService: TokenStorageService, private modalService: BsModalService,
     private alertService: AlertService) { }

  ngOnInit() {
    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = +companyId; // plus je da bi se konvertovao string u broj
  }

  deleteBranchOffice() {
    this.carCompanyService.deleteBranchOffice(this.branchOffice.id, this.companyId).subscribe(
      data => {
        console.log('deleteBranchOffice', this.branchOffice);
        this.branchOfficeDeleted.emit(this.branchOffice.id);
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
      this.alertService.info('Branch office is changed!');
    });
  }
}
