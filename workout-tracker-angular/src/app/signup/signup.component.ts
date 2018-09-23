import { Component } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { RegistrationService } from '../_services/registration.service';
import { SignupModel } from './signup-model';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent {

  private static REG_SCSS_MSG = 'Registration successful';
  private static REG_FAIL_MSG = 'Registration failed! Please try again';

  public registrationFailed = false;
  public registrationMessage = '';
  public registrationForm = new SignupModel();

  public validationPatterns = {
    'firstname': '^[a-zA-Z]{2,}$',
    'lastname': '^[a-zA-Z]{2,}$',
    'email': '^[a-z0-9_%$#\\.\\+\\-]{2,}@[a-zA-Z0-9\\-]{2,}\\.[a-z]{2,}$',
    'password': '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#@$\\-]).{8}$'
  };
  public validPasswordRules = [
    'Password must be exactly 8 characters',
    'Password must contain one of #@$-',
    'Password must contain one uppercase letter',
    'Password must contain one lowercase letter',
    'Password must contain one number'
  ];

  constructor(private signupService: RegistrationService) { }

  onSubmit() {
    this.checkEmailExistsAndRegister(this.registrationForm.email);
  }

  checkEmailExistsAndRegister(email: String) {
    this.signupService.searchEmail(email).subscribe(
      res => this.register(email, res.status === 200 ? true : false),
      err => this.handleErrors(err),
    );
  }

  register(email: String, emailExists: boolean) {
    if (emailExists) {
      this.registrationFailed = true;
      this.registrationMessage = 'Sorry! ' + email  + ' is already registered';
      this.registrationForm.email = '';
    } else {
      this.signupService.registerNewUser(this.registrationForm).subscribe(
        res => this.handleResponse(res),
        err => this.signupService.handleError(err),
      );
    }
  }

  handleErrors(error: HttpErrorResponse): void {
    if (error.error instanceof ErrorEvent) {
      console.log(`Client error :: ${error.error.message}`);
    } else {
      console.log(`Server error [${error.status}]:: ${error.statusText}`);
      console.log(`Error Body : ${JSON.stringify(error.error)}`);
    }
    this.registrationFailed = true;
    this.registrationMessage = String(SignupComponent.REG_FAIL_MSG);
  }

  handleResponse(response: HttpResponse<Object>): void {
    const status = response.status;
    const keys = response.headers.keys();
    const headers = keys.map(key => `${key} : ${response.headers.get(key)}`);
    // headers.forEach(element => { console.log(element); });

    if (status === 201) {
      this.registrationFailed = false;
      this.registrationMessage = String(SignupComponent.REG_SCSS_MSG);
      this.registrationForm = new SignupModel();
    } else if (status === 200) {
      this.registrationFailed = false;
      this.registrationMessage = String(SignupComponent.REG_SCSS_MSG);
      this.registrationForm = new SignupModel();
    } else {
      this.registrationFailed = true;
      this.registrationMessage = String(SignupComponent.REG_FAIL_MSG);
      console.log('Registration Status : ' + status);
      console.log('Registration Failed : ' + this.registrationFailed);
      console.log('Registration Message: ' + this.registrationMessage);
    }
  }

}
