import { Destination } from './../../model/air-company/destination';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { AirCompany } from 'src/app/model/air-company/air-company';
import { User } from 'src/app/model/users/user';

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
}
