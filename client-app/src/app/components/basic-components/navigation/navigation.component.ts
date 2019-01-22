import { Router } from '@angular/router';
import { Component, OnInit} from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { Hotel } from 'src/app/model/hotel/hotel';
import { AirCompany } from 'src/app/model/air-company/air-company';

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

  constructor(public tokenService: TokenStorageService, private router: Router) {
    this.loggedIn = tokenService.checkIsLoggedIn();
    this.tokenService.logggedInEmitter.subscribe(loggedIn => {
      this.loggedIn = loggedIn;
    });
    this.username = tokenService.getUsername();
    this.tokenService.usernameEmitter.subscribe(username => this.username = username);
  }

  ngOnInit() {
  }

  logout() {
    this.router.navigateByUrl('http://localhost:4200');
    this.tokenService.signOut();
  }
}
