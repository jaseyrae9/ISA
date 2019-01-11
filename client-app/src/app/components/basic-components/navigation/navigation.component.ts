import { Component, OnInit} from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { Hotel } from 'src/app/model/hotel/hotel';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { Role } from 'src/app/model/role';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  public loggedIn: Boolean;
  public username: String;
  public carcompanies: RentACarCompany[];
  public hotels: Hotel[];
  public aircompanies: AirCompany[];
  roles: Role[];

  constructor(public tokenService: TokenStorageService) {
    this.loggedIn = tokenService.checkIsLoggedIn();
    this.tokenService.logggedInEmitter.subscribe(loggedIn => {
      this.loggedIn = loggedIn;
    });
    this.username = tokenService.getUsername();
    this.tokenService.usernameEmitter.subscribe(username => this.username = username);
  }

  ngOnInit() {
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  isSysAdmin()
  {
    if (this.roles != undefined) {
      for (let role of this.roles) {
        if (role.authority == 'ROLE_SYS') {
          return true;
        }
      }
      return false;
    }
    return false;
  }
}
