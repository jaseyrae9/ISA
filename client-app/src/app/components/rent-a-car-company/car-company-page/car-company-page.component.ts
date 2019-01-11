import { Component, OnInit } from '@angular/core';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';


@Component({
  selector: 'app-car-company-page',
  templateUrl: './car-company-page.component.html',
  styleUrls: ['./car-company-page.component.css', '../../../shared/css/inputField.css']
})
export class CarCompanyPageComponent implements OnInit {
  carCompany: RentACarCompany = new RentACarCompany();
  forEditing: RentACarCompany = new RentACarCompany();
  roles: Role[];

  constructor(private route: ActivatedRoute, private carService: RentACarCompanyService, public tokenService: TokenStorageService) { }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    this.carService.get(id).subscribe(
      (data) => {
        this.carCompany = data;
        this.forEditing = new RentACarCompany(data.id, data.name, data.description, data.cars);

      }
    );
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  carCreated(car: Car) {
    this.carCompany.cars.push(car);
  }

  carCompanyEdited(data) {
    this.carCompany.id = data.id;
    this.carCompany.name = data.name;
    this.carCompany.description = data.description;
    this.forEditing = new RentACarCompany(data.id, data.name, data.description, data.cars);
  }

  carDeleted(carId: number) {
    for (const car of this.carCompany.cars) {
      if (car.id === carId) {
        car.active = false;
      }
    }
  }

  isCarAdmin() {
    if (this.roles !== null) {
      for (const role of this.roles) {
        if (role.authority === 'ROLE_CARADMIN') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

}
