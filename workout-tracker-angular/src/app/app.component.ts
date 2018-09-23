import { Component } from '@angular/core';
import { ComponentBase } from './_base/base-component';
import { AuthService } from './_services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent extends ComponentBase {

  title = 'Workout Tracker';
  constructor(private authService: AuthService, private router: Router) {
    super(authService);
  }

  logout(): void {
    this.authService.signout();
    this.isUnauthorized$().subscribe(
      (unauthorized: boolean) => {
        if (unauthorized) {
          this.router.navigate([AuthService.publicHome]);
        }
      }
    );
  }

}
