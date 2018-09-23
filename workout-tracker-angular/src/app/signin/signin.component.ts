import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../_services/auth.service';
import { ComponentBase } from '../_base/base-component';
import { SigninModel } from './signin-model';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent extends ComponentBase {

  public signinForm: SigninModel = new SigninModel();

  constructor(private authService: AuthService, private router: Router) {
    super(authService);
  }

  onSubmit(signinForm: SigninModel) {
    this.authService.signin(signinForm);
    this.isUnauthorized$().subscribe(
      (unauthorized: boolean) => {
        if (unauthorized) {
          this.signinForm.password = '';
        } else {
          this.signinForm = new SigninModel();
          this.router.navigate([AuthService.secureHome]);
        }
      }
    );
  }

}
