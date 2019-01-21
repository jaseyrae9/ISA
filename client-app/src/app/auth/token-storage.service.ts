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

  public isAirAdmin = false;

  public logggedInEmitter = this.isLoggedIn.asObservable();
  public usernameEmitter = this.username.asObservable();
  public rolesEmitter = this.roles.asObservable();

  constructor() {
    this.isLoggedIn.next(false);
    this.roles.next(null);
    this.checkRoles();
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
    this.checkRoles();
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
    this.checkRoles();
  }

  public checkRoles() {
    this.isAirAdmin = false;
    const roles = this.getRoles();
    if (roles) {
      for (const role of roles) {
        if ( role.authority === 'ROLE_AIRADMIN') {
          this.isAirAdmin = true;
        }
      }
    }
  }

  public getRoles(): Role[] {
    if (sessionStorage.getItem(ROLES_KEY)) {
      return JSON.parse(sessionStorage.getItem(ROLES_KEY));
    }
    return null;
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
