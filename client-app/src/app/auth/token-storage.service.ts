import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

const TOKEN_KEY = 'AuthToken';
const USERNAME_KEY = 'username';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private isLoggedIn = new Subject<Boolean>();

  constructor() {
    if (this.getToken) {
      this.loggedInEmitChange(true);
    } else {
      this.loggedInEmitChange(false);
    }
  }

  public logggedInEmitter = this.isLoggedIn.asObservable();
  loggedInEmitChange(loggedIn: Boolean) {
      this.isLoggedIn.next(loggedIn);
  }

  signOut() {
    window.sessionStorage.clear();
    this.loggedInEmitChange(false);
  }

  public saveToken(token: string) {
    window.sessionStorage.setItem(TOKEN_KEY, token);
    this.loggedInEmitChange(true);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUsername(username: string) {
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public loggedIn(): Boolean {
    if (this.getToken) {
      return true;
    } else {
      return false;
    }
  }
}
