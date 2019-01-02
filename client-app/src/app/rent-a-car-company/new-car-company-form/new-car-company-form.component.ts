import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { RentACarCompanyService} from 'src/app/services/rent-a-car-company/rent-a-car-company.service';

@Component({
  selector: 'app-new-car-company-form',
  templateUrl: './new-car-company-form.component.html',
  styleUrls: ['../../shared/css/inputField.css']
})
export class NewCarCompanyFormComponent implements OnInit {
  carCompany: RentACarCompany;
  @Output() carCompanyCreated: EventEmitter<RentACarCompany> = new EventEmitter();
  form: any = {};
  @ViewChild('closeBtn') closeBtn: ElementRef;

  constructor(private rentACarCompanyService: RentACarCompanyService) { }

  ngOnInit() {
  }

  onCarCompanyAdd(){
    this.carCompany = new RentACarCompany(null,
      this.form.name,
      this.form.description
  );

  this.rentACarCompanyService.add(this.carCompany).subscribe(
    data => {
      this.carCompanyCreated.emit(data);
      this.closeBtn.nativeElement.click();
    },
    error => {
      console.log(error.error.message);
    }

  );
  }

}
