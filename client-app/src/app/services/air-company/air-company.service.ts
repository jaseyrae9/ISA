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

  add(airCompany: AirCompany): Observable<AirCompany> {
    return this.http.post<AirCompany>('http://localhost:8080/aircompanies/add', airCompany, httpOptions);
  }

  addAdmin(user: User, airId: String): Observable<User> {
    console.log('Dodat admin:');
    console.log(user);
    console.log('Za avio kompaniju' + airId);
    return this.http.post<User>('http://localhost:8080/sys/airCompanyAdmin/' + airId, user, httpOptions);
  }
}
