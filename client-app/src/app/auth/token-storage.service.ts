import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Role } from 'src/app/model/role';

const TOKEN_KEY = 'AuthToken';
const USERNAME_KEY = 'username';
const ROLES_KEY = 'roles';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private isLoggedIn = new Subject<Boolean>();
  private username = new Subject<String>();
  private roles = new Subject<Role[]>();

  public logggedInEmitter = this.isLoggedIn.asObservable();
  public usernameEmitter = this.username.asObservable();
  public rolesEmitter = this.roles.asObservable();

  constructor() {
    this.isLoggedIn.next(false);
    this.roles.next(null);
  }

  loggedInEmitChange(loggedIn: Boolean) {
    this.isLoggedIn.next(loggedIn);
  }

  usernameEmitChange(username: String) {
    this.username.next(username);
  }

  rolesEmitChange(roles: Role[]) {
    this.roles.next(roles);
  }

  signOut() {
    window.sessionStorage.clear();
    this.loggedInEmitChange(false);
    this.usernameEmitChange(null);
    this.rolesEmitChange(null);
  }

  public saveToken(token: string) {
    window.sessionStorage.setItem(TOKEN_KEY, token);
    this.loggedInEmitChange(true);
  }

  public saveUsername(username: string) {
    window.sessionStorage.setItem(USERNAME_KEY, username);
    this.usernameEmitChange(username);
  }

  public saveRoles(roles: Role[]) {
    window.sessionStorage.setItem(ROLES_KEY, JSON.stringify(roles));
    this.rolesEmitChange(roles);
  }

  public getRoles(): Role[] {
    return JSON.parse(sessionStorage.getItem(ROLES_KEY));
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public checkIsLoggedIn(): Boolean {
    // TODO: Promeniti da proveri vazi li token i dalje
    if (sessionStorage.getItem(TOKEN_KEY)) {
      return true;
    } else {
      return false;
    }
  }
}
