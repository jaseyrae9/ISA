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
export class ReservationsService {

  constructor(private http: HttpClient) { }

  reserve(reservationDTO): Observable<any> {
    return this.http.post<AirCompany>('http://localhost:8080/reservation/create', reservationDTO, httpOptions);
  }

  cancel(id): Observable<any> {
    return this.http.put<AirCompany>('http://localhost:8080/reservation/cancle/' + id, httpOptions);
  }
}
