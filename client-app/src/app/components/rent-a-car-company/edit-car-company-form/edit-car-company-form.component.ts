import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';
import { ViewChild, ElementRef } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService} from 'src/app/services/rent-a-car-company/rent-a-car-company.service';


@Component({
  selector: 'app-edit-car-company-form',
  templateUrl: './edit-car-company-form.component.html',
  styleUrls: ['./edit-car-company-form.component.css',  '../../../shared/css/inputField.css']
})
export class EditCarCompanyFormComponent implements OnInit {
  @Output() carCompanyEdited: EventEmitter<RentACarCompany> = new EventEmitter();
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Input() carCompany: RentACarCompany;

  constructor (private carCompanyService: RentACarCompanyService) { }

  ngOnInit() {
  }

  onCarCompanyEdit() {
    this.carCompanyService.edit(this.carCompany).subscribe(
      data => {
        this.carCompanyEdited.emit(data);
        this.closeBtn.nativeElement.click();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
