import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { User } from 'src/app/model/users/user';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';

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

  get(id: string): Observable<any> {
    return this.http.get('//localhost:8080/rent_a_car_companies/get/' + id);
  }

  add(carCompany: RentACarCompany): Observable<RentACarCompany> {
    return this.http.post<RentACarCompany>('http://localhost:8080/rent_a_car_companies/add', carCompany, httpOptions);
  }

  addAdmin(user: User, carCompanyId: String): Observable<User> {
    return this.http.post<User>('http://localhost:8080/sys/rentACarCompanyAdmin/' + carCompanyId, user, httpOptions);
  }

  addCar(car: Car, carCompanyId: String): Observable<Car> {
    return this.http.post<Car>('http://localhost:8080/rent_a_car_companies/addCar/' + carCompanyId, car, httpOptions);
  }

  addBranchOffice(branchOffice: BranchOffice, carCompanyId: String): Observable<BranchOffice> {
    return this.http.post<BranchOffice>('http://localhost:8080/rent_a_car_companies/addBranchOffice/'
    + carCompanyId, branchOffice, httpOptions);
  }

  edit(carCompany: RentACarCompany): Observable<RentACarCompany> {
    return this.http.put<RentACarCompany>('http://localhost:8080/rent_a_car_companies/edit/' + carCompany.id, carCompany, httpOptions);
  }

  editCar(car: Car, carCompanyId: String): Observable<Car> {
    return this.http.put<Car>('http://localhost:8080/rent_a_car_companies/editCar/' + carCompanyId, car, httpOptions);
  }

  delete(carId: number, carCompanyId: String): Observable<number> {
    return this.http.delete<number>('http://localhost:8080/rent_a_car_companies/deleteCar/' + carCompanyId + '/' + carId, httpOptions);
  }

  deleteBranchOffice(branchOfficeId: number, carCompanyId: String): Observable<number> {
    return this.http.delete<number>('http://localhost:8080/rent_a_car_companies/deleteBranchOffice/' + carCompanyId
    + '/' + branchOfficeId, httpOptions);
  }

}
