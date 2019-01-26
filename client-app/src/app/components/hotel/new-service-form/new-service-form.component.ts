import { Component, OnInit, Input } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-new-service-form',
  templateUrl: './new-service-form.component.html',
  styleUrls: ['./new-service-form.component.css', '../../../shared/css/inputField.css']
})
export class NewServiceFormComponent implements OnInit {
  errorMessage: String = '';
  public onClose: Subject<AdditionalService>;
  newAdditionalServiceForm: FormGroup;
  @Input() hotelId: Number;
  additionalService: AdditionalService = new AdditionalService();

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder,
     private hotelService: HotelService) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.newAdditionalServiceForm = this.formBuilder.group({
      name: [this.additionalService.name, [Validators.required]],
      description: [this.additionalService.description],
      price: [this.additionalService.price, [Validators.required, Validators.min(0)]],
      });
  }

  onServiceAdd() {
    this.hotelService.addAdditionService(this.newAdditionalServiceForm.value, this.hotelId).subscribe(
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
