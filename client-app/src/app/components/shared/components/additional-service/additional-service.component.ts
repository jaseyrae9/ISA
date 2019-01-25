import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AdditionalService } from 'src/app/model/additional-service';

@Component({
  selector: 'app-additional-service',
  templateUrl: './additional-service.component.html',
  styleUrls: ['./additional-service.component.css', '../../../../shared/css/item.css', '../../../../shared/css/deleteAndEditLinks.css']
})
export class AdditionalServiceComponent implements OnInit {
  @Input() additionalService: AdditionalService;

  @Output() editEmitter: EventEmitter<AdditionalService> = new EventEmitter();
  @Output() deleteEmitter: EventEmitter<AdditionalService> = new EventEmitter();

  @Input() isAdditionalServicesTab = true;

  isChecked = false;
  @Output() checkEmitter: EventEmitter<AdditionalService> = new EventEmitter();
  @Output() xEmitter: EventEmitter<AdditionalService> = new EventEmitter();
  constructor() { }

  ngOnInit() {
  }

  clickEdit() {
    this.editEmitter.emit(this.additionalService);
  }

  clickDelete() {
    this.deleteEmitter.emit(this.additionalService);
  }

  clickCheck() {
    this.isChecked = true;
    this.checkEmitter.emit(this.additionalService);
  }

  clickX() {
    this.isChecked = false;
    this.xEmitter.emit(this.additionalService);
  }
}
