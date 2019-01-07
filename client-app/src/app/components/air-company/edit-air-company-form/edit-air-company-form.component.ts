import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { ViewChild, ElementRef } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';

@Component({
  selector: 'app-edit-air-company-form',
  templateUrl: './edit-air-company-form.component.html',
  styleUrls: ['./edit-air-company-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditAirCompanyFormComponent implements OnInit {
  @Output() airCompanyEdited: EventEmitter<AirCompany> = new EventEmitter();
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() airCompany: AirCompany;
  editedAirCompany: AirCompany = new AirCompany();


  constructor(private airCompanyService: AirCompanyService) { }

  ngOnInit() {
  }

  onEditAirCompany() {
    this.editedAirCompany.id = this.airCompany.id;
    this.airCompanyService.edit(this.editedAirCompany).subscribe(
      data => {
        this.airCompanyEdited.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
