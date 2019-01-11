import { Component, OnInit, Input, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';

@Component({
  selector: 'app-edit-car-form',
  templateUrl: './edit-car-form.component.html',
  styleUrls: ['./edit-car-form.component.css', '../../../shared/css/inputField.css']
})
export class EditCarFormComponent implements OnInit {
  @Input() car: Car;
  @Input() companyId: string;
  public onClose: Subject<Car>;

  constructor(private carCompanyService: RentACarCompanyService, public modalRef: BsModalRef) { }

  ngOnInit() {
    this.onClose = new Subject();
  }

  onCarEdit() {
    this.carCompanyService.editCar(this.car, this.companyId).subscribe(
      data => {
        this.onClose.next(this.car);
        this.modalRef.hide();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }

}
