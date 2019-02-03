import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { ChangePasswordData } from 'src/app/model/users/changePassword';


import { Observable } from 'rxjs/Observable';
import { map, catchError } from 'rxjs/operators';
import { User } from 'src/app/model/users/user';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http: HttpClient) { }

  getCurrentUserProfile(): Observable<any> {
      return this.http.get('//localhost:8080/profile/info');
  }

  changePassword(data: ChangePasswordData, jwtToken: String = '' ): Observable<any> {
    if (jwtToken.length > 0) {
      // za admine koji moraju prvi put da promene sifru
      const differentHttpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + jwtToken })
      };
      return this.http.post<ChangePasswordData>('//localhost:8080/profile/changePassword', data, differentHttpOptions);
    } else {
      return this.http.post<ChangePasswordData>('//localhost:8080/profile/changePassword', data, httpOptions);
    }
  }

  updateProfile(info: User): Observable<any> {
    return this.http.post<ChangePasswordData>('//localhost:8080/profile/updateProfile', info, httpOptions);
  }

  getFriendRequests(pageNumber: number, sortBy): Observable<any> {
    return this.http.get('//localhost:8080/friendship/friendRequests?page=' + pageNumber + '&size=2' + '&sort=' + sortBy);
  }

  getFriends(pageNumber: number, sortBy): Observable<any> {
    return this.http.get('//localhost:8080/friendship/friends?page=' + pageNumber + '&size=2' + '&sort=' + sortBy);
  }

  acceptFriendRequest(fromId: number): Observable<any> {
    return this.http.get('//localhost:8080/friendship/acceptRequest/' + fromId);
  }

  deleteFriendship(fromId: number): Observable<any> {
    return this.http.get('//localhost:8080/friendship/delete/' + fromId);
  }

  sendRequest(to: number): Observable<any> {
    return this.http.get('//localhost:8080/friendship/sendRequest/' + to);
  }

  searchCustomers(searchTerm: string, pageNumber: number, sortBy): Observable<any> {
    // tslint:disable-next-line:max-line-length
    return this.http.get('//localhost:8080/friendship/search?searchTerm=' + searchTerm + '&page=' + pageNumber + '&size=2' + '&sort=' + sortBy);
  }
  getReservations(): Observable<any> {
    return this.http.get('//localhost:8080/customers/getAllReservations');
  }

}
