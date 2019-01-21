import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Hotel } from '../../model/hotel/hotel';
import { User } from 'src/app/model/users/user';
import { Room } from 'src/app/model/hotel/room';
import { AdditionalService } from 'src/app/model/additional-service';

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

  get(id: string): Observable<any> {
    return this.http.get('//localhost:8080/hotels/get/' + id);
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

  edit(hotel: Hotel): Observable<Hotel> {
    console.log(hotel);
    return this.http.put<Hotel>('http://localhost:8080/hotels/edit/' + hotel.id, hotel, httpOptions);
  }

  addRoom(room: Room, hotelId: String): Observable<Room> {
    return this.http.post<Room>('http://localhost:8080/hotels/addRoom/' + hotelId, room, httpOptions);
  }

  editRoom(room: Room, hotelId: String): Observable<Room> {
     return this.http.put<Room>('http://localhost:8080/hotels/editRoom/' + hotelId, room, httpOptions);
   }

  delete(roomId: number, hotelId: String): Observable<number> {
    return this.http.delete<number>('http://localhost:8080/hotels/deleteRoom/' + hotelId + '/' + roomId, httpOptions);
  }

  addAdditionService(additionalService: AdditionalService, hotelId: String): Observable<AdditionalService> {
    return this.http.post<AdditionalService>('http://localhost:8080/hotels/addAdditionalService/' + hotelId,
     additionalService, httpOptions);
  }

  editAdditionalService(additionalService: AdditionalService, hotelId: number): Observable<AdditionalService> {
    return this.http.put<AdditionalService>('http://localhost:8080/hotels/editAdditionalService/' + hotelId,
     additionalService, httpOptions);
  }

  deleteAdditionalService(serviceId: number, hotelId: number): Observable<number> {
    return this.http.delete<number>('http://localhost:8080/hotels/deleteAdditionalService/' + hotelId + '/' + serviceId, httpOptions);
  }
}
