import { Component, OnInit, Input } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';
import { Subject } from 'rxjs/Subject';
import { HotelService } from 'src/app/services/hotel/hotel.service';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-edit-service-form',
  templateUrl: './edit-service-form.component.html',
  styleUrls: ['./edit-service-form.component.css', '../../../shared/css/inputField.css']
})
export class EditServiceFormComponent implements OnInit {
  errorMessage: String = '';
  @Input() additionalService: AdditionalService;
  @Input() hotelId: string;

  public onClose: Subject<AdditionalService>;

  editServiceForm: FormGroup;


  constructor(private hotelService: HotelService, public modalRef: BsModalRef, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.onClose = new Subject();
    this.editServiceForm = this.formBuilder.group({
      id: [this.additionalService.id],
      name: [this.additionalService.name, [Validators.required]],
      description: [this.additionalService.description],
      price: [this.additionalService.price, [Validators.required]]
    });
  }

  onEditAdditionalService() {
    this.hotelService.editAdditionalService(this.editServiceForm.value, this.hotelId).subscribe(
      data => {
        console.log('promenjeno', data);
        this.onClose.next(data);
        this.modalRef.hide();
      },
      error => {
        console.log(error.error.message);
      }
    );
  }
}
