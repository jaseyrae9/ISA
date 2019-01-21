import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { DestinationFormComponent } from './../destination-form/destination-form.component';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { Destination } from './../../../model/air-company/destination';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { TokenStorageService } from 'src/app/auth/token-storage.service';

@Component({
  selector: 'app-destination',
  templateUrl: './destination.component.html',
  styleUrls: ['./destination.component.css', '../../../shared/css/deleteAndEditLinks.css']
})
export class DestinationComponent implements OnInit {
  @Input() destination: Destination = new Destination();
  @Input() airportId: Number = 0;
  @Output() destinationEvent: EventEmitter<Object> = new EventEmitter();
  modalRef: BsModalRef;

  constructor(private modalService: BsModalService, private airService: AirCompanyService, public tokenService: TokenStorageService) { }

  ngOnInit() {
  }

  openEditModal() {
    const initialState = {
      destination: this.destination,
      aircompanyId: this.airportId
    };
    this.modalRef = this.modalService.show(DestinationFormComponent, { initialState });
    this.modalRef.content.onClose.subscribe(destination => {
      this.destination = destination;
    });
  }

  deleteDestination() {
    this.airService.deleteDestination(this.destination.id, this.airportId).subscribe(
      (data) => {
        this.destinationEvent.next(this.destination);
      }
    );
  }

}
