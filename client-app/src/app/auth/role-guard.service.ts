import { Injectable } from '@angular/core';
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot
} from '@angular/router';
import { TokenStorageService } from './token-storage.service';
import * as decode from 'jwt-decode';
import { TokenPayload } from 'src/app/model/token-payload';

@Injectable()
export class RoleGuardService implements CanActivate {
  constructor(public tokenStorageService: TokenStorageService, public router: Router) { }
  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (!this.tokenStorageService.checkIsLoggedIn()) {
      this.router.navigate(['']);
      // console.log('Ne moze');
      return false;
    }
    // this will be passed from the route config
    // on the data property
    const expectedRoles = route.data.expectedRoles;
    const token = sessionStorage.getItem('AuthToken');
    // decode the token to get its payload
    const tokenPayload: TokenPayload = decode(token);
    console.log(tokenPayload);

    let canAccess = false;


    console.log(tokenPayload.roles);

    // console.log('expectedRoles' + expectedRoles);
    for (const role of tokenPayload.roles) {
      for (const expectedRole of expectedRoles) {
        if (role.authority === 'ROLE_' + expectedRole) {
          canAccess = true;
          // console.log('Moze');
          break;
        }
      }
    }

    return canAccess;
  }
}
