import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { EditCarFormComponent } from '../edit-car-form/edit-car-form.component';
import { ActivatedRoute } from '@angular/router';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { AlertService } from 'ngx-alerts';
import { HttpErrorResponse } from '@angular/common/http';
import { FastTicketFormComponent } from '../fast-ticket-form/fast-ticket-form.component';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

@Component({
  selector: 'app-car-basic-info',
  templateUrl: './car-basic-info.component.html',
  styleUrls: ['./car-basic-info.component.css', '../../../shared/css/inputField.css']
})
export class CarBasicInfoComponent implements OnInit {
  @Input() car: Car;
  @Input() carCompany: RentACarCompany;

  @Output() carDeleted: EventEmitter<number> = new EventEmitter();
  @Output() carFasten: EventEmitter<number> = new EventEmitter();
  @Output() carRented: EventEmitter<Car> = new EventEmitter();

  @Input() isCarsTab = true;

  modalRef: BsModalRef;

  constructor(public tokenService: TokenStorageService,
    private modalService: BsModalService, public carCompanyService: RentACarCompanyService,
    private alertService: AlertService) { }

  ngOnInit() {
  }

  openFastReservationModal() {
    const initialState = {
      car: this.car,
      carCompany: this.carCompany
    };
    this.modalRef = this.modalService.show(FastTicketFormComponent, { initialState });
     this.modalRef.content.onClose.subscribe(car => {
      console.log('fast ', car);
      this.carFasten.emit(this.car.id);
      this.car = car;
      this.alertService.info('Car is added for fast reservations!');
    });
  }

  // izmena auta
  openEditModal() {
    const initialState = {
      car: this.car,
      companyId: this.carCompany.id
    };
    this.modalRef = this.modalService.show(EditCarFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(car => {
      this.car = car;
      this.alertService.info('Car is changed!');
    });
  }

  deleteCar() {
    this.carCompanyService.delete(this.car.id, this.carCompany.id).subscribe(
      data => {
        console.log('Brisanje automobila', this.car);
        this.carDeleted.emit(this.car.id);
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.alertService.info(err.error.details);
      }
    );
  }

  onRent() {
    this.carRented.emit(this.car); // salje auto na koji se klikne
  }

}
