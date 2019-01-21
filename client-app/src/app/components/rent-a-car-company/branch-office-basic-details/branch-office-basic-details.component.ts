import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Role } from 'src/app/model/role';
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
  @Output() branchOfficeDeleted: EventEmitter<number> = new EventEmitter();

  carCompany: RentACarCompany = new RentACarCompany();
  roles: Role[];

  private forEditing: BranchOffice;
  modalRef: BsModalRef;
  companyId: string;

  constructor(private route: ActivatedRoute, private carCompanyService: RentACarCompanyService,
     public tokenService: TokenStorageService, private modalService: BsModalService,
     public ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = companyId;
    // const id = this.route.snapshot.paramMap.get('id');
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);

    // for edit
    this.forEditing = new BranchOffice(this.branchOffice.id, this.branchOffice.name
      , this.branchOffice.active);
  }

  deleteBranchOffice() {
    this.carCompanyService.deleteBranchOffice(this.branchOffice.id, this.companyId).subscribe(
      data => {
        this.branchOfficeDeleted.emit(data);
      },
      error => {
        console.log(error);
      }
    );
  }

  openEditModal() {
    const initialState = {
      branchOffice: this.forEditing,
      companyId: this.companyId
    };
    this.modalRef = this.modalService.show(EditBranchOfficeFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(result => {
      this.branchOfficeEdited(result);
    });
  }

  branchOfficeEdited(data) {
    this.branchOffice.id = data.id;
    this.branchOffice.name = data.name;
    this.branchOffice.active = data.active;
    this.forEditing = new BranchOffice(this.branchOffice.id, this.branchOffice.name,
      this.branchOffice.active);
    this.ngxNotificationService.sendMessage('Branch office is changed!', 'dark', 'bottom-right' );
  }

  isCarAdmin() {
    if (this.roles !== null) {
      for (const role of this.roles) {
        if (role.authority === 'ROLE_CARADMIN') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

}
