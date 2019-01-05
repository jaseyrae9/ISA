import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { ViewChild, ElementRef } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service'

@Component({
  selector: 'app-edit-company-form',
  templateUrl: './edit-company-form.component.html',
  styleUrls: ['./edit-company-form.component.css',  '../../shared/css/inputField.css']
})
export class EditCompanyFormComponent implements OnInit {
  @Output() airCompanyEdited: EventEmitter<AirCompany> = new EventEmitter();
  
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() airCompany: AirCompany;


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
        console.log(error.error.message);
      }
    );
  }

}
