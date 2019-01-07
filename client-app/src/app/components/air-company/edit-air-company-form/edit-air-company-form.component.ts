import { AirCompany } from './../../../model/air-company/air-company';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { ViewChild, ElementRef } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { NgModule } from '@angular/core';

@Component({
  selector: 'app-edit-air-company-form',
  templateUrl: './edit-air-company-form.component.html',
  styleUrls: ['./edit-air-company-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditAirCompanyFormComponent implements OnInit {
  @Output() airCompanyEdited: EventEmitter<AirCompany> = new EventEmitter();
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() airCompany: AirCompany;
  errorMessage: String = '';

  constructor(private airCompanyService: AirCompanyService) { }

  ngOnInit() {
  }

  onEditAirCompany() {
    this.airCompanyService.edit(this.airCompany).subscribe(
      data => {
        this.airCompanyEdited.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        // interceptor je hendlovao ove zahteve
        if (error.status === 401 || error.status === 403 || error.status === 404) {
          this.closeBtn.nativeElement.click();
        }
        this.errorMessage = error.error.details;
      }
    );
  }

}
