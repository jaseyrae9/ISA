import { AirplaneFormComponent } from './../airplane-form/airplane-form.component';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AirCompanyService } from './../../../../services/air-company/air-company.service';
import { Airplane } from './../../../../model/air-company/airplane';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AlertService } from 'ngx-alerts';

@Component({
  selector: 'app-airplane-display',
  templateUrl: './airplane-display.component.html',
  styleUrls: ['./airplane-display.component.css', '../../../../shared/css/seats.css',
  '../../../../shared/css/deleteAndEditLinks.css', '../../../../shared/css/item.css']
})
export class AirplaneDisplayComponent implements OnInit {
  @Input() airplane: Airplane;
  @Input() companyId: Number;
  @Input() useButtons = true;
  @Output() airplaneEvent: EventEmitter<Object> = new EventEmitter();
  modalRef: BsModalRef;

  constructor(private modalService: BsModalService, private alertService: AlertService,
    private airCompanyService: AirCompanyService) { }

  ngOnInit() {
  }

  deleteAirplane() {
    this.airCompanyService.deleteAirplane(this.companyId, this.airplane.id).subscribe(
      () => {
        this.alertService.info('Airplane ' + this.airplane.name + ' deleted.');
        this.airplaneEvent.emit(this.airplane);
      }
    );
  }

  activateAirplane() {
    this.airCompanyService.activateAirplane(this.companyId, this.airplane.id).subscribe(
      (data) => {
        this.alertService.info('Airplane ' + this.airplane.name + ' activated.');
        this.airplane.status = status;
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
      this.alertService.info('Airpane ' + this.airplane.name + ' edited.');
      this.airplane.name = airplane.name;
      this.airplane.rowNum = airplane.rowNum;
      this.airplane.colNum = airplane.colNum;
      this.airplane.seatsPerCol = airplane.seatsPerCol;
      this.airplane.seats = airplane.seats;
    });
  }
}
