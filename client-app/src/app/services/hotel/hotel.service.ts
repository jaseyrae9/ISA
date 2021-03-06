import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Hotel } from '../../model/hotel/hotel';
import { User } from 'src/app/model/users/user';
import { Room } from 'src/app/model/hotel/room';
import { AdditionalService } from 'src/app/model/additional-service';
import { ReservationRequest } from 'src/app/model/hotel/reservation-request';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class HotelService {

  constructor(private http: HttpClient) { }

  getAllHotels(): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/hotels/allHotels');
  }

  getAll(page, sort): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/hotels/all?page=' + page + '&size=2&sort=' + sort);
  }

  get(id): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/hotels/get/' + id);
  }

  add(hotel: Hotel): Observable<Hotel> {
    console.log(hotel);
    return this.http.post<Hotel>('https://isa-back.herokuapp.com/hotels/add', hotel, httpOptions);
  }

  addAdmin(user: User, hotelId: String): Observable<User> {
    return this.http.post<User>('https://isa-back.herokuapp.com/sys/hotelAdmin/' + hotelId, user, httpOptions);
  }

  edit(hotel: Hotel): Observable<Hotel> {
    console.log(hotel);
    return this.http.put<Hotel>('https://isa-back.herokuapp.com/hotels/edit/' + hotel.id, hotel, httpOptions);
  }

  addRoom(room: Room, hotelId: Number): Observable<Room> {
    return this.http.post<Room>('https://isa-back.herokuapp.com/hotels/addRoom/' + hotelId, room, httpOptions);
  }

  editRoom(room: Room, hotelId: String): Observable<Room> {
     return this.http.put<Room>('https://isa-back.herokuapp.com/hotels/editRoom/' + hotelId, room, httpOptions);
   }

  delete(roomId: number, hotelId): Observable<number> {
    return this.http.delete<number>('https://isa-back.herokuapp.com/hotels/deleteRoom/' + hotelId + '/' + roomId, httpOptions);
  }

  addAdditionService(additionalService: AdditionalService, hotelId: Number): Observable<AdditionalService> {
    return this.http.post<AdditionalService>('https://isa-back.herokuapp.com/hotels/addAdditionalService/' + hotelId,
     additionalService, httpOptions);
  }

  editAdditionalService(additionalService: AdditionalService, hotelId: String): Observable<AdditionalService> {
    console.log(additionalService);
    return this.http.put<AdditionalService>('https://isa-back.herokuapp.com/hotels/editAdditionalService/' + hotelId,
    additionalService, httpOptions);
  }

  deleteAdditionalService(serviceId: number, hotelId: number): Observable<number> {
    return this.http.delete<number>('https://isa-back.herokuapp.com/hotels/deleteAdditionalService/'
    + hotelId + '/' + serviceId, httpOptions);
  }

  rentRoom(roomReservation: ReservationRequest, hotelId: number, customer: string) {
    return this.http.post<ReservationRequest>('https://isa-back.herokuapp.com/hotels/rentRoom/' + hotelId + '/' + customer,
     roomReservation, httpOptions);
  }

  getSearchAll(name: String, address: String, checkInDate: String, checkOutDate: String): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/hotels/search?hotelName=' + name + '&hotelAddress='
    + address + '&hotelCheckInDate=' + checkInDate + '&hotelCheckOutDate=' + checkOutDate);
  }

  getMonthlyVisitation(hotelId): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/hotelMonthly/' + hotelId, httpOptions);
  }

  getWeeklyVisitation(hotelId): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/hotelWeekly/' + hotelId, httpOptions);
  }

  getDailyVisitation(hotelId): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/hotelDaily/' + hotelId, httpOptions);
  }

  getIncome(hotelId, startDate, endDate): Observable<any> {
    return this.http.get<any>('https://isa-back.herokuapp.com/report_info/hotelIncome/'
    + hotelId + '/' + startDate + '/' + endDate, httpOptions);
  }

  rateHotel(hotelId, reservationId, rate): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/reservation/rateHotel/'
    + hotelId + '/' + reservationId + '/' + rate, httpOptions);
  }

  rateRoom(roomId, singleReservationId, rate): Observable<any> {
    return this.http.put<any>('https://isa-back.herokuapp.com/reservation/rateRoom/'
    + roomId + '/' + singleReservationId + '/' + rate, httpOptions);
  }

  addServiceToFast(hotelId, serviceId) {
    return this.http.put<any>('https://isa-back.herokuapp.com/hotels/addServiceToFast/'
    + hotelId + '/' + serviceId, httpOptions);
  }

  removeServiceFromFast(hotelId, serviceId) {
    return this.http.put<any>('https://isa-back.herokuapp.com/hotels/removeServiceFromFast/'
    + hotelId + '/' + serviceId, httpOptions);
  }

  getRoomsForFastReservation(hotelId): Observable<Room[]> {
    return this.http.get<any>('https://isa-back.herokuapp.com/hotels/getFastRooms/' + hotelId, httpOptions);
  }

  makeRoomFast(hotelId, roomId, startDate, endDate, discount): Observable<Room> {
    return this.http.put<Room>('https://isa-back.herokuapp.com/hotels/makeRoomFast/'
     + hotelId + '/' + roomId + '/' + discount + '/' + startDate + '/' + endDate, httpOptions);
  }

  makeRoomSlow(hotelId, roomId): Observable<Room> {
    return this.http.put<Room>('https://isa-back.herokuapp.com/hotels/makeRoomSlow/'
     + hotelId + '/' + roomId, httpOptions);
  }

  getFastRoomsSearch(city, date, tikcetCount): Observable<Room[]> {
    return this.http.get<Room[]>('https://isa-back.herokuapp.com/hotels/getFastRooms/'
    + city + '/' + date + '/' + tikcetCount, httpOptions);
  }

  cancelRoomReservation(id): Observable<number> {
    console.log('pokusaj otkazivanja room reservation');
    return this.http.delete<number>('https://isa-back.herokuapp.com/hotels/cancelRoomReservation/' + id, httpOptions);
  }

}
