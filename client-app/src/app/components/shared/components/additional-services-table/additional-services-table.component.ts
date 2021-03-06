import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';

@Component({
  selector: 'app-additional-services-table',
  templateUrl: './additional-services-table.component.html',
  styleUrls: ['./additional-services-table.component.css']
})
export class AdditionalServicesTableComponent implements OnInit {
  @Input() companyId = -10;
  @Input() additionalServices: AdditionalService[];

  @Output() editEmitter: EventEmitter<AdditionalService> = new EventEmitter();
  @Output() deleteEmitter: EventEmitter<AdditionalService> = new EventEmitter();
  @Output() fastEmitter: EventEmitter<AdditionalService> = new EventEmitter();
  @Output() fastRemoveEmitter: EventEmitter<AdditionalService> = new EventEmitter();

  @Output() checkEmitter: EventEmitter<AdditionalService> = new EventEmitter();
  @Output() xEmitter: EventEmitter<AdditionalService> = new EventEmitter();

  @Input() isAdditionalServicesTab = true;

  constructor() { }

  ngOnInit() {
  }

  clickEdit(data) {
    this.editEmitter.emit(data);
  }

  clickDelete(data) {
    this.deleteEmitter.emit(data);
  }

  clickCheck(data) {
    this.checkEmitter.emit(data);
    console.log('check ', data);
  }

  clickX(data) {
    this.xEmitter.emit(data);
    console.log('x ', data);
  }

  clickFast(data) {
    this.fastEmitter.emit(data);
    console.log('fast ', data);
  }

  clickFastRemove(data) {
    this.fastRemoveEmitter.emit(data);
    console.log('fast remove ', data);
  }
}
