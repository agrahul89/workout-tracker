import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class TokenHelperService {

  private static AUTH_SERVICE: AuthService;
  private static JWT_HELPER: JwtHelperService;

  constructor(private authService: AuthService,  private jwtHelper: JwtHelperService) {
    TokenHelperService.AUTH_SERVICE = authService;
    TokenHelperService.JWT_HELPER = jwtHelper;
  }

  static tokenExpired(): boolean {
    console.log(TokenHelperService.JWT_HELPER.getTokenExpirationDate(TokenHelperService.AUTH_SERVICE.auth));
    return TokenHelperService.JWT_HELPER.isTokenExpired(TokenHelperService.AUTH_SERVICE.auth);
  }

}
