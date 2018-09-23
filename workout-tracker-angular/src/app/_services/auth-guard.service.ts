import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { takeLast, map } from 'rxjs/operators';
import { TokenHelperService } from './token-helper.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  private authenticated$ = new BehaviorSubject<boolean>(false);

  constructor(
    private authService: AuthService,
    private tokenHelper: TokenHelperService,
    private router: Router) { }

  isAuthenticated(): Observable<boolean> {
    return this.authenticated$.asObservable();
  }

  /* TODO: use AuthGuard implementation */
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {

    if (!this.authService.auth) {
      this.router.navigate([AuthService.publicHome]);
    }

    return this.isAuthenticated().pipe(
      takeLast(1),
      map((isLoggedIn: boolean) => {
        if (isLoggedIn) {
          this.router.navigate([AuthService.secureHome]);
        } else {
          this.router.navigate([AuthService.publicHome]);
        }
        return isLoggedIn;
      })
    );
  }

}
