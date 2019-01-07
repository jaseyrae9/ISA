import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

const TOKEN_KEY = 'AuthToken';
const USERNAME_KEY = 'username';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private isLoggedIn = new Subject<Boolean>();
  private username = new Subject<String>();
  public logggedInEmitter = this.isLoggedIn.asObservable();
  public usernameEmitter = this.username.asObservable();

  constructor() {
    this.isLoggedIn.next(false);
  }

  loggedInEmitChange(loggedIn: Boolean) {
      this.isLoggedIn.next(loggedIn);
  }

  usernameEmitChange(username: String) {
      this.username.next(username);
  }

  signOut() {
    window.sessionStorage.clear();
    this.loggedInEmitChange(false);
    this.usernameEmitChange(null);
  }

  public saveToken(token: string) {
    window.sessionStorage.setItem(TOKEN_KEY, token);
    this.loggedInEmitChange(true);
  }

  public saveUsername(username: string) {
    window.sessionStorage.setItem(USERNAME_KEY, username);
    this.usernameEmitChange(username);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public checkIsLoggedIn(): Boolean {
    // TODO: Promeniti da proveri vazi li token i dalje
    if ( sessionStorage.getItem(TOKEN_KEY) ) {
      return true;
    } else {
      return false;
    }
  }
}
