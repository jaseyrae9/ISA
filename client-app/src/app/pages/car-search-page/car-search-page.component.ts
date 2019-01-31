import { Component, OnInit } from '@angular/core';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { FormBuilder, FormGroup } from '@angular/forms';
import { formatDate } from '@angular/common';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

@Component({
  selector: 'app-car-search-page',
  templateUrl: './car-search-page.component.html',
  styleUrls: ['./car-search-page.component.css', '../../shared/css/inputField.css']
})
export class CarSearchPageComponent implements OnInit {
  datePickerConfig: Partial<BsDatepickerConfig>;
  errorMessage: String = '';
  searchCompanyForm: FormGroup;
  bsRangeValue: Date[];
  companies: RentACarCompany[] = [];

  constructor(private rentACarCompanyService: RentACarCompanyService,
    private formBuilder: FormBuilder) {
    this.datePickerConfig = Object.assign({},
      {
        containerClass: 'theme-default',
        dateInputFormat: 'YYYY-MM-DD'
      });
  }

  ngOnInit() {
    this.rentACarCompanyService.getAllCompanies().subscribe(data => {
      this.companies = data;
    });

    this.searchCompanyForm = this.formBuilder.group({
      companyName: [''],
      bsRangeValue: [this.bsRangeValue],
      companyAddress: [''],
    });
  }

  onSearch() {
    let pickUpDate = '';
    let dropOffDate = '';

    if (this.searchCompanyForm.value.bsRangeValue !== null) {
      pickUpDate = formatDate(this.searchCompanyForm.value.bsRangeValue[0], 'yyyy-MM-dd', 'en');
      dropOffDate = formatDate(this.searchCompanyForm.value.bsRangeValue[1], 'yyyy-MM-dd', 'en');
    }

    this.rentACarCompanyService.getAllSearched(this.searchCompanyForm.value.companyName,
    this.searchCompanyForm.value.companyAddress,
    pickUpDate,
    dropOffDate).subscribe(
      data => {
        this.companies = data;
        console.log('pretragaaa');
    });
  }

}
