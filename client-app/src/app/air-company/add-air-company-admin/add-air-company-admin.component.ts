import { Component, OnInit, Input } from '@angular/core';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { User } from 'src/app/model/users/user';
import { NgForm } from '@angular/forms';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service'

@Component({
  selector: 'app-add-air-company-admin',
  templateUrl: './add-air-company-admin.component.html',
  styleUrls: ['./add-air-company-admin.component.css']
})
export class AddAirCompanyAdminComponent implements OnInit {
  @Input() aircompanies: AirCompany[];

  private user : User;

  constructor(private airCompanyService: AirCompanyService) { }
 
  ngOnInit() {
  }

  onSubmit(form : NgForm)
  {
    console.log(form);

    this.user = new User("changeme",form.value.firstName, form.value.lastName, form.value.email, form.value.phoneNumber, form.value.address);
    
    this.airCompanyService.addAdmin(this.user, form.value.aircompany).subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log(error);
      }
    );

  }

}
