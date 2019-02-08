import { Flight } from './../../model/air-company/flight';
import { AdditionalService } from './../../model/additional-service';
import { Destination } from './../../model/air-company/destination';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { User } from 'src/app/model/users/user';
import { Airplane } from 'src/app/model/air-company/airplane';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AirCompanyService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/aircompanies/all');
  }

  getAllCompanies(pageNumber, sortBy): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/aircompanies/allAirCompanies?page='
    + pageNumber + '&size=2&sort=' + sortBy);
  }

  get(id: string): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/aircompanies/get/' + id);
  }

  add(airCompany: AirCompany): Observable<AirCompany> {
    return this.http.post<AirCompany>('https://isa-back.herokuapp.com/aircompanies/add', airCompany, httpOptions);
  }

  addAdmin(user: User, airId: String): Observable<User> {
    return this.http.post<User>('https://isa-back.herokuapp.com/sys/airCompanyAdmin/' + airId, user, httpOptions);
  }

  edit(airCompany: AirCompany): Observable<AirCompany> {
    return this.http.put<AirCompany>('https://isa-back.herokuapp.com/aircompanies/edit/'
    + airCompany.id, airCompany, httpOptions);
  }

  addDestination(destination, id): Observable<Destination> {
    return this.http.post<Destination>('https://isa-back.herokuapp.com/aircompanies/addDestination/'
    + id, destination, httpOptions);
  }

  deleteDestination(destination, id): Observable<any> {
    return this.http.delete<Object>('https://isa-back.herokuapp.com/aircompanies/deleteDestination/' + id + '/' + destination);
  }

  editDestination(destination, id): Observable<Destination> {
    return this.http.put<Destination>('https://isa-back.herokuapp.com/aircompanies/editDestination/'
    + id, destination, httpOptions);
  }

  addBaggageInformation(info, id): Observable<AdditionalService> {
    return this.http.post<AdditionalService>('https://isa-back.herokuapp.com/aircompanies/addBaggageInformation/'
    + id, info, httpOptions);
  }

  deleteBaggageInformation(baggageId, id): Observable<any> {
    return this.http.delete<Object>('https://isa-back.herokuapp.com/aircompanies/deleteBaggageInformation/'
    + id + '/' + baggageId);
  }

  editBaggageInformation(info, id): Observable<AdditionalService> {
    return this.http.put<AdditionalService>('https://isa-back.herokuapp.com/aircompanies/editBaggageInformation/'
    + id, info, httpOptions);
  }

  getAllAirplanes(id): Observable<Airplane[]> {
    return this.http.get<Airplane[]>('https://isa-back.herokuapp.com/aircompanies/getAirplanes/' + id);
  }

  getActiveAirplanes(id): Observable<Airplane[]> {
    return this.http.get<Airplane[]>('https://isa-back.herokuapp.com/aircompanies/getActiveAirplanes/' + id);
  }

  deleteAirplane(company, airplane): Observable<any> {
    return this.http.delete<any>('https://isa-back.herokuapp.com/aircompanies/deleteAirplane/' + company + '/' + airplane);
  }

  activateAirplane(company, airplane): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/aircompanies/activateAirplane/'
    + company + '/' + airplane, null, httpOptions);
  }

  addAirplane(company, airplane): Observable<Airplane> {
    return this.http.post<Airplane>('https://isa-back.herokuapp.com/aircompanies/addAirplane/'
    + company, airplane, httpOptions);
  }

  editAirplane(company, airplane): Observable<Airplane> {
    return this.http.put<Airplane>('https://isa-back.herokuapp.com/aircompanies/editAirplane/'
    + company, airplane, httpOptions);
  }

  searchFlights(data): Observable<Flight[]> {
    return this.http.post<Flight[]>('https://isa-back.herokuapp.com/aircompanies/searchFlights', data, httpOptions);
  }

  getFlights(company, page): Observable<Flight[]> {
    return this.http.get<Flight[]>('https://isa-back.herokuapp.com/aircompanies/getFlights/'
    + company + '?page=' + page + '&size=' + 2);
  }

  getFlight(id): Observable<Flight> {
    return this.http.get<Flight>('https://isa-back.herokuapp.com/aircompanies/getFlight/' + id);
  }

  addFlight(company, flight): Observable<Flight> {
    return this.http.post<Flight>('https://isa-back.herokuapp.com/aircompanies/addFlight/' + company, flight, httpOptions);
  }

  editFlight(company, id, flight): Observable<Flight> {
    return this.http.put<Flight>('https://isa-back.herokuapp.com/aircompanies/editFlight/'
    + company + '/' + id, flight, httpOptions);
  }

  changeTicketsPrices(company, id, prices): Observable<Flight> {
    return this.http.put<Flight>('https://isa-back.herokuapp.com/aircompanies/changePrices/'
    + company + '/' + id, prices, httpOptions);
  }

  deleteFlight(company, flight): Observable<any> {
    return this.http.delete<any>('https://isa-back.herokuapp.com/aircompanies/deleteFlight/' + company + '/' + flight);
  }

  activateFlight(company, flight): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/aircompanies/activateFlight/'
    + company + '/' + flight, httpOptions);
  }

  disableSeats(company, flight, tickets): Observable<any> {
    return this.http.put<Flight>('https://isa-back.herokuapp.com/aircompanies/disableTicket/'
    + company + '/' + flight, tickets, httpOptions);
  }

  createTicketsForFastReservation(company, flight, data): Observable<any> {
    return this.http.post<any>('https://isa-back.herokuapp.com/aircompanies/createTickets/' + company + '/' + flight,
     data, httpOptions);
  }

  deleteTicketForFastReservation(company, ticket) {
    return this.http.delete<any>('https://isa-back.herokuapp.com/aircompanies/deleteTicket/' + company + '/' + ticket);
  }

  getTicketsForFastReservation(company): Observable<any> {
    return this.http.get<Flight>('https://isa-back.herokuapp.com/aircompanies/getTickets/' + company);
  }

  fastReservation(id, passport) {
    return this.http.post<any>('https://isa-back.herokuapp.com/aircompanies/fastReservation/' + id, passport, httpOptions);
  }

  getMonthly(id): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/aircompanies/getSoldTicketsPerMonth/' + id);
  }

  getWeekly(id): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/aircompanies/getSoldTicketsPerWeek/' + id);
  }

  getDaily(id): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/aircompanies/getSoldTicketsPerDay/' + id);
  }

  getProfit(id, from, to): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/aircompanies/getProfitInPeriod/'
    + id + '?from=' + from + '&to=' + to);
  }

  rateAirCompany(airCompanyId, reservationId, rate): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/reservation/rateAirCompany/'
    + airCompanyId + '/' + reservationId + '/' + rate, httpOptions);
  }

  rateFlight(flightId, reservationId, rate): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/reservation/rateFlight/'
    + flightId + '/' + reservationId + '/' + rate, httpOptions);
  }
}
