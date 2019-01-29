import { ActivatedRoute, Router } from '@angular/router';
import { AirCompanyService } from './../../../../services/air-company/air-company.service';
import { Flight } from './../../../../model/air-company/flight';
import { Component, OnInit, Input } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-flight-page',
  templateUrl: './flight-page.component.html',
  styleUrls: ['./flight-page.component.css', '../../../../shared/css/inputField.css', '../../../../shared/css/deleteAndEditLinks.css']
})
export class FlightPageComponent implements OnInit {
  flight: Flight = new Flight();
  constructor(private ngxNotificationService: NgxNotificationService, private router: Router, private airService: AirCompanyService ,
    public tokenService: TokenStorageService, private route: ActivatedRoute) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.airService.getFlight(id).subscribe(
      (data) => {
        this.flight = data;
      }
    );
  }

  deleteFlight () {
    this.airService.deleteFlight(this.flight.airCompanyBasicInfo.id, this.flight.id).subscribe(
      (data) => {
        this.router.navigate(['/aircompany', this.flight.airCompanyBasicInfo.id]);
      }
      // neuspesno brisanje ce handlovati interceptori
    );
  }

  activateFlight() {
    this.airService.activateFlight(this.flight.airCompanyBasicInfo.id, this.flight.id).subscribe(
      (data) => {
        this.ngxNotificationService.sendMessage('Flight activated.', 'dark', 'bottom-right');
        this.flight.status = data.status;
      }
      // neuspesnu aktivaciju ce handlovati interceptori
    );
  }

}
