import { HttpErrorResponse } from '@angular/common/http';
import { AirCompanyService } from './../../../../services/air-company/air-company.service';
import { Flight } from './../../../../model/air-company/flight';
import { Subject } from 'rxjs/Subject';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-change-tickets-prices-form',
  templateUrl: './change-tickets-prices-form.component.html',
  styleUrls: ['./change-tickets-prices-form.component.css', './../../../../shared/css/inputField.css']
})
export class ChangeTicketsPricesFormComponent implements OnInit {
  @Input() flight: Flight = new Flight();
  errorMessage: String = '';
  pricesForm: FormGroup;
  public onClose: Subject<Flight>;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder, private aircompanyService: AirCompanyService) { }

  ngOnInit() {
    this.pricesForm = this.formBuilder.group({
      economyPrice: [this.flight.economyPrice, [Validators.required, Validators.min(0)]],
      premiumEconomyPrice: [this.flight.premiumEconomyPrice, [Validators.required, Validators.min(0)]],
      bussinessPrice: [this.flight.bussinessPrice, [Validators.required, Validators.min(0)]],
      firstPrice: [this.flight.firstPrice, [Validators.required, Validators.min(0)]]
    });
    this.onClose = new Subject();
  }

  submit() {
    this.aircompanyService.changeTicketsPrices(this.flight.airCompanyBasicInfo.id, this.flight.id, this.pricesForm.value).subscribe(
      (data) => {
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
