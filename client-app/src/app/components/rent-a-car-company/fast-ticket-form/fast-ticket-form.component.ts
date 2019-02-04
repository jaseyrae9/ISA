import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Subject } from 'rxjs/Subject';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { BsDatepickerConfig } from 'ngx-bootstrap';
import { RentACarCompanyService } from 'src/app/services/rent-a-car-company/rent-a-car-company.service';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-fast-ticket-form',
  templateUrl: './fast-ticket-form.component.html',
  styleUrls: ['./fast-ticket-form.component.css', '../../../shared/css/inputField.css']
})
export class FastTicketFormComponent implements OnInit {
  errorMessage: String = '';
  @Input() car: Car;
  public onClose: Subject<Car>;
  makeFastForm: FormGroup;
  datePickerConfig: Partial<BsDatepickerConfig>;
  bsRangeValue: Date[];
  @Input() carCompanyId: number;

  constructor(public modalRef: BsModalRef,
    private formBuilder: FormBuilder,
    private rentACarCompanyService: RentACarCompanyService) {
      this.datePickerConfig = Object.assign({},
        {
          containerClass: 'theme-default',
          dateInputFormat: 'YYYY-MM-DD'
        });
    }

  ngOnInit() {
    this.onClose = new Subject();
    this.makeFastForm = this.formBuilder.group({
      numberOfPassengers: ['', [Validators.min(0)]],
      dicount: ['', [Validators.min(0)]],
      bsRangeValue: [this.bsRangeValue],
    });
    console.log('UNDEF CHECK ', this.car);
  }

  onMakeFast() {
    console.log('dugme make fast');

    const date0 = new Date(this.makeFastForm.value.bsRangeValue[0]);
    const datum0 = formatDate(date0, 'yyyy-MM-dd', 'en');
    const date1 = new Date(this.makeFastForm.value.bsRangeValue[1]);
    const datum1 = formatDate(date1, 'yyyy-MM-dd', 'en');


    this.rentACarCompanyService.addCarToFastReservations(this.carCompanyId, this.car.id, this.makeFastForm.value.dicount,
      datum0, datum1).subscribe(
      data => {
        this.onClose.next(data);
        this.modalRef.hide();
      },
      (err: HttpErrorResponse) => {
        // interceptor je hendlovao ove zahteve
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.modalRef.hide();
        }
        this.errorMessage = err.error.details;
      }
    );

  }

}
