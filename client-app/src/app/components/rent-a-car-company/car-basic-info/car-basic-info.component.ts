import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { EditCarFormComponent } from '../edit-car-form/edit-car-form.component';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';

@Component({
  selector: 'app-car-basic-info',
  templateUrl: './car-basic-info.component.html',
  styleUrls: ['./car-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class CarBasicInfoComponent implements OnInit {
  @Input() car: Car;
  @Output() carDeleted: EventEmitter<number> = new EventEmitter();

  roles: Role[];
  private forEditing: Car;
  modalRef: BsModalRef;
  companyId: string;

  constructor(private route: ActivatedRoute, public tokenService: TokenStorageService,
  private modalService: BsModalService, public carCompanyService: RentACarCompanyService) { }

  ngOnInit() {
    this.forEditing = new Car(this.car.id, this.car.type, this.car.brand,
    this.car.model, this.car.seatsNumber, this.car.doorsNumber,
    this.car.yearOfProduction, this.car.price, this.car.active);

    this.roles = this.tokenService.getRoles();
    this.roles = this.tokenService.getRoles();

    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = companyId;
  }

  openEditModal() {
    const initialState = {
      car: this.forEditing,
      companyId: this.companyId
    };
    this.modalRef = this.modalService.show(EditCarFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(result => {
      this.carEdited(result);
    });
  }

  carEdited(data) {
    this.car.id = data.id;
    this.car.brand = data.brand;
    this.car.model = data.model;
    this.car.doorsNumber = data.doorsNumber;
    this.car.seatsNumber = data.seatsNumber;
    this.car.yearOfProduction = data.yearOfProduction;
    this.car.price = data.price;
    this.car.type = data.type;
    this.car.active = data.active;
    this.forEditing = new Car(this.car.id, this.car.type, this.car.brand, this.car.model,
                              this.car.seatsNumber, this.car.doorsNumber,
                              this.car.yearOfProduction, this.car.price, this.car.active);
  }

  deleteCar() {
    this.carCompanyService.delete(this.car.id, this.companyId).subscribe(
      data => {
        this.carDeleted.emit(data);
      },
      error => {
        console.log(error);
      }
    );
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
