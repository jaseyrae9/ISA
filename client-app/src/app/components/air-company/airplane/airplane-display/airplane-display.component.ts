import { AirplaneFormComponent } from './../airplane-form/airplane-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AirCompanyService } from './../../../../services/air-company/air-company.service';
import { Airplane } from './../../../../model/air-company/airplane';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NgxNotificationService } from 'ngx-notification';

@Component({
  selector: 'app-airplane-display',
  templateUrl: './airplane-display.component.html',
  styleUrls: ['./airplane-display.component.css', '../../../../shared/css/seats.css',
  '../../../../shared/css/deleteAndEditLinks.css', '../../../../shared/css/item.css']
})
export class AirplaneDisplayComponent implements OnInit {
  @Input() airplane: Airplane;
  @Input() companyId: Number;
  @Output() airplaneEvent: EventEmitter<Object> = new EventEmitter();
  modalRef: BsModalRef;

  constructor(private modalService: BsModalService, private ngxNotificationService: NgxNotificationService,
    private airCompanyService: AirCompanyService) { }

  ngOnInit() {
  }

  deleteAirplane() {
    this.airCompanyService.deleteAirplane(this.companyId, this.airplane.id).subscribe(
      () => {
        this.ngxNotificationService.sendMessage('Airplane ' + this.airplane.name + ' deleted.', 'dark', 'bottom-right');
        this.airplaneEvent.emit(this.airplane);
      }
    );
  }

  activateAirplane() {
    this.airCompanyService.activateAirplane(this.companyId, this.airplane.id).subscribe(
      (data) => {
        this.ngxNotificationService.sendMessage('Airplane ' + this.airplane.name + ' activated.', 'dark', 'bottom-right');
        this.airplane = data;
      }
    );
  }

  editAirplane() {
    const initialState = {
      airplane: this.airplane,
      aircompanyId: this.companyId
    };
    this.modalRef = this.modalService.show(AirplaneFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(airplane => {
      this.ngxNotificationService.sendMessage('Airpane ' + this.airplane.name + ' edited.', 'dark', 'bottom-right');
      this.airplane = airplane;
    });
  }
}
