import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  changePassword(data: ChangePasswordData): Observable<any> {
    return this.http.post<ChangePasswordData>('//localhost:8080/profile/changePassword', data, httpOptions);
  }

  updateProfile(info: User): Observable<any> {
    return this.http.post<ChangePasswordData>('//localhost:8080/profile/updateProfile', info, httpOptions);
  }

  getFriendRequests(pageNumber: number): Observable<any> {
    return this.http.get('//localhost:8080/friendship/friendRequests?page=' + pageNumber + '&size=2');
  }

  getFriends(pageNumber: number): Observable<any> {
    return this.http.get('//localhost:8080/friendship/friends?page=' + pageNumber + '&size=2');
  }

  acceptFriendRequest(fromId: number): Observable<any> {
    return this.http.get('//localhost:8080/friendship/acceptRequest/' + fromId);
  }

  deleteFriendship(fromId: number): Observable<any> {
    return this.http.get('//localhost:8080/friendship/delete/' + fromId);
  }
}
