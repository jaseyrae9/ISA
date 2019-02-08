import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { RentACarCompany } from 'src/app/model/rent-a-car-company/rent-a-car-company';
import { User } from 'src/app/model/users/user';
import { Car } from 'src/app/model/rent-a-car-company/car';
import { BranchOffice } from 'src/app/model/rent-a-car-company/branch-offfice';
import { CarReservation } from 'src/app/model/rent-a-car-company/car-reservation';
import { formatDate } from '@angular/common';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class RentACarCompanyService {

  constructor(private http: HttpClient) { }

  getAll(pageNumber, sortBy): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/rent_a_car_companies/all?page='
    + pageNumber + '&size=2&sort=' + sortBy);
  }

  getAllCompanies(): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/rent_a_car_companies/allCompanies');
  }
  get(id): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/rent_a_car_companies/get/' + id);
  }

  add(carCompany: RentACarCompany): Observable<RentACarCompany> {
    console.log('Add car company:', carCompany);
    return this.http.post<RentACarCompany>('https://isa-back.herokuapp.com/rent_a_car_companies/add', carCompany, httpOptions);
  }

  addAdmin(user: User, carCompanyId: String): Observable<User> {
    return this.http.post<User>('https://isa-back.herokuapp.com/sys/rentACarCompanyAdmin/' + carCompanyId, user, httpOptions);
  }

  addCar(car: Car, carCompanyId: Number): Observable<Car> {
    return this.http.post<Car>('https://isa-back.herokuapp.com/rent_a_car_companies/addCar/' + carCompanyId, car, httpOptions);
  }

  addBranchOffice(branchOffice: BranchOffice, carCompanyId: Number): Observable<BranchOffice> {
    return this.http.post<BranchOffice>('https://isa-back.herokuapp.com/rent_a_car_companies/addBranchOffice/'
    + carCompanyId, branchOffice, httpOptions);
  }

  editBranchOffice(branchOffice: BranchOffice, carCompanyId: String): Observable<BranchOffice> {
    return this.http.put<BranchOffice>('https://isa-back.herokuapp.com/rent_a_car_companies/editBranchOffice/'
     + carCompanyId, branchOffice, httpOptions);
  }

  edit(carCompany: RentACarCompany): Observable<RentACarCompany> {
    return this.http.put<RentACarCompany>('https://isa-back.herokuapp.com/rent_a_car_companies/edit/'
    + carCompany.id, carCompany, httpOptions);
  }

  editCar(car: Car, carCompanyId: String): Observable<Car> {
    return this.http.put<Car>('https://isa-back.herokuapp.com/rent_a_car_companies/editCar/' + carCompanyId, car, httpOptions);
  }

  delete(carId: number, carCompanyId): Observable<number> {
    return this.http.delete<number>('https://isa-back.herokuapp.com/rent_a_car_companies/deleteCar/'
    + carCompanyId + '/' + carId, httpOptions);
  }


  deleteBranchOffice(branchOfficeId: number, carCompanyId): Observable<number> {
    return this.http.delete<number>('https://isa-back.herokuapp.com/rent_a_car_companies/deleteBranchOffice/' + carCompanyId
    + '/' + branchOfficeId, httpOptions);
  }

  rentCar(carReservation: CarReservation, carCompanyId: number, carId: number, customer: string): Observable<CarReservation> {
    console.log(carReservation);

    return this.http.post<CarReservation>('https://isa-back.herokuapp.com/rent_a_car_companies/rentCar/' + carCompanyId + '/'
    + carReservation.pickUpBranchOffice.id + '/' + carReservation.dropOffBranchOffice.id + '/'
    + formatDate(carReservation.pickUpDate, 'yyyy-MM-dd', 'en')
    + '/' + formatDate(carReservation.dropOffDate, 'yyyy-MM-dd', 'en') + '/' + carId + '/' + customer, httpOptions);
  }

  getAllSearched(companyName: String, companyAddress: String, pickUpDate: String, dropOffDate: String): Observable<any> {
    return this.http.get<CarReservation>('https://isa-back.herokuapp.com/rent_a_car_companies/search?name=' + companyName
    + '&address=' + companyAddress + '&pickUpDate=' + pickUpDate + '&dropOffDate=' + dropOffDate, httpOptions);
  }
  getMonthlyResevations(companyId): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/rentACarCompanyMonthly/' + companyId, httpOptions);
  }
  getWeeklyResevations(companyId): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/rentACarCompanyWeekly/' + companyId, httpOptions);
  }
  getDailyResevations(companyId): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/rentACarCompanyDaily/' + companyId, httpOptions);
  }

  rateCarCompany(carCompanyId, reservationId, rate): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/reservation/rateCarCompany/'
    + carCompanyId + '/' + reservationId + '/' + rate, httpOptions);
  }

  rateCar(carId, reservationId, rate): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/reservation/rateCar/'
    + carId + '/' + reservationId + '/' + rate, httpOptions);
  }

  getIncome(companyId, startDate, endDate): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/rentACarCompanyIncome/' + companyId
    + '/' + startDate + '/' + endDate, httpOptions);
  }

  getFastCars(companyId): Observable<Car[]> {
    return this.http.get<Car[]>('https://isa-back.herokuapp.com/rent_a_car_companies/getFastCars/' + companyId, httpOptions);
  }

  getFastCarsSearch(city, date): Observable<Car[]> {
    return this.http.get<Car[]>('https://isa-back.herokuapp.com/rent_a_car_companies/getFastCars/'
    + city + '/' + date, httpOptions);
  }

  addCarToFastReservations(companyId, carId, discount, beginDate, endDate, bo1, bo2): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/rent_a_car_companies/addCarToFastReservation/'
    + companyId + '/' + carId + '/' + discount + '/' + beginDate + '/' + endDate + '/' + bo1 + '/' + bo2, httpOptions);

  }

  removeCarFromFastReservations(companyId, carId): Observable<number> {
    return this.http.delete<number>('https://isa-back.herokuapp.com/rent_a_car_companies/removeCarFromFastReservation/'
    + companyId + '/'
    + carId, httpOptions);
  }

  cancelCarReservation(id): Observable<number> {
    return this.http.delete<number>('https://isa-back.herokuapp.com/rent_a_car_companies/cancelCarReservation/'
    + id, httpOptions);
  }
}
