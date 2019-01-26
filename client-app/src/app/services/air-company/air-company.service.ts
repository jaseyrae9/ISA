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
    return this.http.get('//localhost:8080/aircompanies/all');
  }

  get(id: string): Observable<any> {
    return this.http.get('//localhost:8080/aircompanies/get/' + id);
  }

  add(airCompany: AirCompany): Observable<AirCompany> {
    return this.http.post<AirCompany>('http://localhost:8080/aircompanies/add', airCompany, httpOptions);
  }

  addAdmin(user: User, airId: String): Observable<User> {
    return this.http.post<User>('http://localhost:8080/sys/airCompanyAdmin/' + airId, user, httpOptions);
  }

  edit(airCompany: AirCompany): Observable<AirCompany> {
    return this.http.put<AirCompany>('http://localhost:8080/aircompanies/edit/' + airCompany.id, airCompany, httpOptions);
  }

  addDestination(destination, id): Observable<Destination> {
    return this.http.post<Destination>('http://localhost:8080/aircompanies/addDestination/' + id, destination, httpOptions);
  }

  deleteDestination(destination, id): Observable<any> {
    return this.http.delete<Object>('http://localhost:8080/aircompanies/deleteDestination/' + id + '/' + destination);
  }

  editDestination(destination, id): Observable<Destination> {
    return this.http.put<Destination>('http://localhost:8080/aircompanies/editDestination/' + id, destination, httpOptions);
  }

  addBaggageInformation(info, id): Observable<AdditionalService> {
    return this.http.post<AdditionalService>('http://localhost:8080/aircompanies/addBaggageInformation/' + id, info, httpOptions);
  }

  deleteBaggageInformation(baggageId, id): Observable<any> {
    return this.http.delete<Object>('http://localhost:8080/aircompanies/deleteBaggageInformation/' + id + '/' + baggageId);
  }

  editBaggageInformation(info, id): Observable<AdditionalService> {
    return this.http.put<AdditionalService>('http://localhost:8080/aircompanies/editBaggageInformation/' + id, info, httpOptions);
  }

  getAllAirplanes(id): Observable<Airplane[]> {
    return this.http.get<Airplane[]>('http://localhost:8080/aircompanies/getAirplanes/' + id);
  }

  deleteAirplane(company, airplane): Observable<any> {
    return this.http.delete<any>('http://localhost:8080/aircompanies/deleteAirplane/' + company + '/' + airplane);
  }

  activateAirplane(company, airplane): Observable<any> {
    return this.http.put<any>('http://localhost:8080/aircompanies/activateAirplane/' + company + '/' + airplane, null, httpOptions);
  }

  addAirplane(company, airplane): Observable<Airplane> {
    return this.http.post<Airplane>('http://localhost:8080/aircompanies/addAirplane/' + company, airplane, httpOptions);
  }

  editAirplane(company, airplane): Observable<Airplane> {
    return this.http.put<Airplane>('http://localhost:8080/aircompanies/editAirplane/' + company, airplane, httpOptions);
  }
}
