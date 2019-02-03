import { Subject } from 'rxjs/Subject';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-reservation-information',
  templateUrl: './reservation-information.component.html',
  styleUrls: ['./reservation-information.component.css', '../../../../shared/css/inputField.css']
})
export class ReservationInformationComponent implements OnInit {
  @Input() isFastReservation: Boolean = false;
  public onClose: Subject<any>;
  form: FormGroup;

  constructor(public modalRef: BsModalRef, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.onClose = new Subject();
    if (this.isFastReservation) {
      this.form = this.formBuilder.group({
        passport: ['', [Validators.required]]
      });
    } else {
      this.form = this.formBuilder.group({
        firstName: ['', [Validators.required]],
        lastName: ['', [Validators.required]],
        passport: ['', [Validators.required]]
      });
    }
  }

  submit() {
    this.onClose.next(this.form.value);
    this.modalRef.hide();
  }

}
