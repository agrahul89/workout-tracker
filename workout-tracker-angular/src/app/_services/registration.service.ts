import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ServiceBase } from '../_base/base-service';
import { SignupModel } from '../signup/signup-model';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService extends ServiceBase {

  private   signupUrl: String = this.baseUrl + '/registration';

  constructor(private client: HttpClient) {
    super(client);
  }

  registerNewUser(signupForm: SignupModel) {
    return this.client.post(
      this.signupUrl.toString(),
      JSON.stringify(signupForm),
      {
        headers: {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json',
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  searchEmail(email: String) {
    const emailSearchUrl = this.signupUrl + '/email/' + email + '/';
    return this.client.get(
      emailSearchUrl.toString(),
      {
        headers: {
          'Accept' : 'application/json',
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

}
