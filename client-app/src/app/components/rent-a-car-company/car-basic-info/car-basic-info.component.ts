import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Role } from 'src/app/model/role';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { EditCarFormComponent } from '../edit-car-form/edit-car-form.component';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-car-basic-info',
  templateUrl: './car-basic-info.component.html',
  styleUrls: ['./car-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class CarBasicInfoComponent implements OnInit {
  @Input() car: Car;

  @Output() carDeleted: EventEmitter<number> = new EventEmitter();

  roles: Role[];

  modalRef: BsModalRef;
  companyId: string;

  constructor(private route: ActivatedRoute, public tokenService: TokenStorageService,
  private modalService: BsModalService, public carCompanyService: RentACarCompanyService,
  public ngxNotificationService: NgxNotificationService) { }

  ngOnInit() {
    this.roles = this.tokenService.getRoles();

    const companyId = this.route.snapshot.paramMap.get('id');
    this.companyId = companyId;
  }

  openEditModal() {
    console.log('Tip auta: ' + this.car.type);
    const initialState = {
      car: this.car,
      companyId: this.companyId
    };
    this.modalRef = this.modalService.show(EditCarFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(car => {
      this.car = car;
      this.ngxNotificationService.sendMessage('Car is changed!', 'dark', 'bottom-right' );
    });
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
}
