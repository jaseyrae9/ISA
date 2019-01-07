import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { Car } from 'src/app/model/rent-a-car-company/car';

@Component({
  selector: 'app-car-company-page',
  templateUrl: './car-company-page.component.html',
  styleUrls: ['./car-company-page.component.css', '../../../shared/css/inputField.css']
})
export class CarCompanyPageComponent implements OnInit {
  carCompany: RentACarCompany = new RentACarCompany();

  constructor(private route: ActivatedRoute, private carService: RentACarCompanyService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.carService.get(id).subscribe(
      (data) => {
        this.carCompany = data;
        console.log(this.carCompany);
      }
    );
  }

  carCreated(car: Car) {
    this.carCompany.cars.push(car);
  }

}
