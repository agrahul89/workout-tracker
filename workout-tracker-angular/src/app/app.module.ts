import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { MomentModule } from 'ngx-moment';

import { AppComponent } from './app.component';
import { RoutingRoutes } from './routing.routing';
import { SigninComponent } from './signin/signin.component';
import { SignupComponent } from './signup/signup.component';
import { WorkoutComponent } from './workout/workout.component';
import { CategoryComponent } from './category/category.component';
import { WorkoutPlusComponent } from './workout-plus/workout-plus.component';
import { RegistrationService } from './_services/registration.service';
import { ChartComponent } from './chart/chart.component';

@NgModule({
   declarations: [
      AppComponent,
      SigninComponent,
      SignupComponent,
      WorkoutComponent,
      CategoryComponent,
      WorkoutPlusComponent,
      ChartComponent
   ],
   entryComponents: [
      WorkoutPlusComponent
   ],
   imports: [
      BrowserModule,
      FormsModule,
      HttpClientModule,
      BsDatepickerModule.forRoot(),
      MomentModule,
      NgbModule.forRoot(),
      ReactiveFormsModule,
      RoutingRoutes
   ],
   providers: [
      RegistrationService
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
