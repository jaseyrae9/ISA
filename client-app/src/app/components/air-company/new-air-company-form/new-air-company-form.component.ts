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

  constructor(private airCompanyService: AirCompanyService) { }

  ngOnInit() {}

  onAddAirCompany() {
    console.log(this.airCompany);
    this.airCompanyService.add(this.airCompany).subscribe(data => {
      this.airCompanyCreated.emit(data);
      this.closeBtn.nativeElement.click();
      },
      (err: HttpErrorResponse) => {
        console.log(err);
      }
    );
  }
}
