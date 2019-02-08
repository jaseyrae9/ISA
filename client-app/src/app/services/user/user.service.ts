import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest } from '@angular/common/http';
import { ChangePasswordData } from 'src/app/model/users/changePassword';


import { Observable } from 'rxjs/Observable';
import { map, catchError } from 'rxjs/operators';
import { User } from 'src/app/model/users/user';
import { Invite } from 'src/app/model/users/invite';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http: HttpClient) { }

  getCurrentUserProfile(): Observable<any> {
      return this.http.get('https://isa-back.herokuapp.com/profile/info');
  }

  changePassword(data: ChangePasswordData, jwtToken: String = '' ): Observable<any> {
    if (jwtToken.length > 0) {
      // za admine koji moraju prvi put da promene sifru
      const differentHttpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + jwtToken })
      };
      return this.http.post<ChangePasswordData>('https://isa-back.herokuapp.com/profile/changePassword',
      data, differentHttpOptions);
    } else {
      return this.http.post<ChangePasswordData>('https://isa-back.herokuapp.com/profile/changePassword', data, httpOptions);
    }
  }

  updateProfile(info: User): Observable<any> {
    return this.http.post<ChangePasswordData>('https://isa-back.herokuapp.com/profile/updateProfile', info, httpOptions);
  }

  getFriendRequests(pageNumber: number, sortBy): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/friendship/friendRequests?page='
    + pageNumber + '&size=2' + '&sort=' + sortBy);
  }

  getFriends(pageNumber: number, sortBy): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/friendship/friends?page='
    + pageNumber + '&size=2' + '&sort=' + sortBy);
  }

  getAllFriends(): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/friendship/friends');
  }

  acceptFriendRequest(fromId: number): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/friendship/acceptRequest/' + fromId);
  }

  deleteFriendship(fromId: number): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/friendship/delete/' + fromId);
  }

  sendRequest(to: number): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/friendship/sendRequest/' + to);
  }

  searchCustomers(searchTerm: string, pageNumber: number, sortBy): Observable<any> {
    // tslint:disable-next-line:max-line-length
    return this.http.get('https://isa-back.herokuapp.com/friendship/search?searchTerm=' + searchTerm + '&page=' + pageNumber + '&size=2' + '&sort=' + sortBy);
  }
  getReservations(): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/customers/getAllReservations');
  }

  getInvites(): Observable<any> {
    return this.http.get('https://isa-back.herokuapp.com/customers/getInvites');
  }

  accpetInvite(id): Observable<any> {
    return this.http.put('https://isa-back.herokuapp.com/customers/acceptInvite/' + id, httpOptions);
  }

  refuseInvite(id): Observable<any> {
    return this.http.put('https://isa-back.herokuapp.com/customers/refuseInvite/' + id, httpOptions);
  }

  addSysAdmin(user): Observable<User> {
    console.log('usao u user service, user: ', user);
    return this.http.post<User>('https://isa-back.herokuapp.com/sys/addSystemAdmin', user, httpOptions);
  }
}
