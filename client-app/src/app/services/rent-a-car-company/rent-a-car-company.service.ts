import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { User } from 'src/app/model/users/user';

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

  addAdmin(user: User, carCompanyId: String): Observable<User> {
    console.log('Dodat admin:');
    console.log(user);
    console.log('Za car company' + carCompanyId);
    return this.http.post<User>('http://localhost:8080/sys/rentACarCompanyAdmin/' + carCompanyId, user, httpOptions);
  }

}
