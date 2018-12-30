import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Hotel } from '../../model/hotel/hotel';
import { User } from 'src/app/model/users/user';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class HotelService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<any> {
    return this.http.get('http://localhost:8080/hotels/all');
  }

  add(hotel: Hotel): Observable<Hotel> {
    console.log(hotel);
    return this.http.post<Hotel>('http://localhost:8080/hotels/add', hotel, httpOptions);
  }

  addAdmin(user: User, hotelId: String): Observable<User> {
    console.log('Dodat admin:');
    console.log(user);
    console.log('Za hoted' + hotelId);
    return this.http.post<User>('http://localhost:8080/sys/hotelAdmin/' + hotelId, user, httpOptions);
  }
}
