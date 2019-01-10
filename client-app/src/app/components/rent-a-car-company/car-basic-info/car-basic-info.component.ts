import { Component, OnInit, Input } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';


@Component({
  selector: 'app-car-basic-info',
  templateUrl: './car-basic-info.component.html',
  styleUrls: ['./car-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class CarBasicInfoComponent implements OnInit {
  @Input() car: Car;
  roles: Role[];
  private forEditing : Car;

  constructor(public tokenService: TokenStorageService) { }

  ngOnInit() {
    this.forEditing = new Car(this.car.id, this.car.type, this.car.brand, this.car.model, this.car.seatsNumber, this.car.doorsNumber, this.car.yearOfProduction, this.car.price);
    this.roles = this.tokenService.getRoles();
    this.tokenService.rolesEmitter.subscribe(roles => this.roles = roles);
  }

  click()
  {
    console.log("Kliknuto na: " + this.car.model);
  }

  getCar()
  {
    return this.forEditing;
  }

  carEdited(data)
  {
    this.car.id = data.id;
    this.car.brand = data.brand;
    this.car.model = data.model;
    this.car.doorsNumber = data.doorsNumber;
    this.car.seatsNumber = data.seatsNumber;
    this.car.yearOfProduction = data.yearOfProduction;
    this.car.price = data.price;
    this.forEditing = new Car(this.car.id,this.car.type, this.car.brand, this.car.model, this.car.seatsNumber, this.car.doorsNumber, this.car.yearOfProduction, this.car.price);
  }

  isCarAdmin() {
    if (this.roles != undefined) {
      for (let role of this.roles) {
        if (role.authority == 'ROLE_CARADMIN') {
          return true;
        }
      }
      return false;
    }
    return false;
  }

}
