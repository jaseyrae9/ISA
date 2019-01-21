import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { Role } from 'src/app/model/role';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

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
  companyId: string;

  constructor(private route: ActivatedRoute, private carCompanyService: RentACarCompanyService,
     public tokenService: TokenStorageService) { }

  ngOnInit() {
    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = companyId;
    // const id = this.route.snapshot.paramMap.get('id');
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
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
