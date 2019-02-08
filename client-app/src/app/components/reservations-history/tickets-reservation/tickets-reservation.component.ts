import { ReservationsService } from 'src/app/services/reservations.service';
import { UserService } from 'src/app/services/user/user.service';
import { FlightReservation } from './../../../model/air-company/flight-reservation';
import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AirCompanyService } from 'src/app/services/air-company/air-company.service';
import { AlertService } from 'ngx-alerts';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-tickets-reservation',
  templateUrl: './tickets-reservation.component.html',
  styleUrls: ['./tickets-reservation.component.css', './../../../shared/css/inputField.css']
})
export class TicketsReservationComponent implements OnInit {
  @Input() flightReservation: FlightReservation = new FlightReservation();
  @Input() reservationId: number = null;
  @Input() useCancel = true;
  @Output() cancelEvent: EventEmitter<Object> = new EventEmitter();

  flightAverage = 0;

  constructor(private airService: AirCompanyService,
    private alertService: AlertService, private reservationService: ReservationsService) { }

  ngOnInit() {
    this.flightAverage = this.flightReservation.flight.totalRating / this.flightReservation.flight.ratingCount;
  }

  cancelReservation() {
    this.reservationService.cancel(this.reservationId).subscribe(
      (data) => {
        this.cancelEvent.emit(data);
        this.alertService.info('Trip canceled.');
      },
      (error) => {
        this.alertService.info('Error occured.');
      }
    );
  }

  rateAirCompany() {
    console.log('Rate air company');
    if (this.flightReservation.isCompanyRated !== true) {
      console.log('Ocenjivanje kompanije', this.flightReservation.flight.airCompanyBasicInfo.averageRating);

      this.airService.rateAirCompany(this.flightReservation.flight.airCompanyBasicInfo.id,
         this.flightReservation.id, this.flightReservation.flight.airCompanyBasicInfo.averageRating).subscribe(
        newRate => {
          console.log('vraceno', newRate);
          this.flightReservation.isCompanyRated = true;
          this.alertService.info('You have rated a air company!');
        },
        (err: HttpErrorResponse) => {
          this.alertService.info(err.error.details);
        }
      );
    }
  }

  rateFlight() {
    console.log('Rate flight');
    if (this.flightReservation.isFlightRated !== true) {
      console.log('Ocenjivanje leta', this.flightAverage);

      this.airService.rateFlight(this.flightReservation.flight.id,
        this.flightReservation.id, this.flightAverage).subscribe(
       newRate => {
         console.log('vraceno', newRate);
         this.flightReservation.isFlightRated = true;
         this.alertService.info('You have rated a flight!');
       },
       (err: HttpErrorResponse) => {
         this.alertService.info(err.error.details);
       }
     );
    }
  }
}
