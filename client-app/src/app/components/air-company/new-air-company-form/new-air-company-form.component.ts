import { LocationService } from './../../../services/location-service';
import { Component, OnInit, EventEmitter, Output} from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { ViewChild, ElementRef} from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-air-company-form',
  templateUrl: './new-air-company-form.component.html',
  styleUrls: ['../../../shared/css/inputField.css']
})
export class NewAirCompanyFormComponent implements OnInit {
  airCompany: AirCompany = new AirCompany();
  @Output() airCompanyCreated: EventEmitter<AirCompany> = new EventEmitter();
  @ViewChild('closeBtn') closeBtn: ElementRef;
  errorMessage: String = '';

  constructor(private airCompanyService: AirCompanyService, private locationService: LocationService) { }

  ngOnInit() {}

  onAddAirCompany() {
    this.locationService.decode(this.airCompany.location.address).subscribe(
      (data) => {
        this.airCompany.location.lat = data.results[0].geometry.location.lat;
        this.airCompany.location.lon = data.results[0].geometry.location.lng;
        this.add();
      },
      (error) => {
        this.airCompany.location.lat = 0;
        this.airCompany.location.lon = 0;
        this.add();
      }
    );
  }

  add() {
    this.airCompanyService.add(this.airCompany).subscribe(data => {
      this.airCompanyCreated.emit(data);
      this.closeBtn.nativeElement.click();
      },
      (err: HttpErrorResponse) => {
        console.log(err);
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.closeBtn.nativeElement.click();
        }
        this.errorMessage = err.error.details;
      }
    );
  }
}
