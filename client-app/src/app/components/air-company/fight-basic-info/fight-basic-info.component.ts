import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';

@Component({
  selector: 'app-fight-basic-info',
  templateUrl: './fight-basic-info.component.html',
  styleUrls: ['./fight-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class FightBasicInfoComponent implements OnInit {
  roles: Role[];

  constructor(public tokenService: TokenStorageService) { }

  ngOnInit() {
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  isAirAdmin() {
    if (this.roles != undefined) {
      for (let role of this.roles) {
        if (role.authority == 'ROLE_AIRADMIN') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

  isCustomer() {
    if (this.roles != undefined) {
      for (let role of this.roles) {
        if (role.authority == 'ROLE_CUSTOMER') {
          return true;
        }
      }
      return false;
    }
    return false;
  }


}
