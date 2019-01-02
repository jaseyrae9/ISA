import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class RentACarCompanyService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get('//localhost:8080/rent_a_car_companies/all');
  }

  add(carCompany: RentACarCompany) : Observable<RentACarCompany>{
    console.log(carCompany);
    return this.http.post<RentACarCompany>('http://localhost:8080/rent_a_car_companies/add', carCompany, httpOptions);
  }

  
}
