import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { CategoryComponent } from './category/category.component';
import { SignupComponent } from './signup/signup.component';
import { SigninComponent } from './signin/signin.component';
import { WorkoutComponent } from './workout/workout.component';
import { ChartComponent } from './chart/chart.component';

const routes: Routes = [
  // TODO Implement Authentication/Routing Guard
  {path: '', pathMatch: 'full', redirectTo: 'signin'/* , canActivate: [AuthService] */},
  {path: 'signin',   component: SigninComponent},
  {path: 'signup',   component: SignupComponent},
  {path: 'logout',   component: AppComponent},
  {path: 'workout',  component: WorkoutComponent},
  {path: 'category', component: CategoryComponent},
  {path: 'track',    component: ChartComponent},
];

export const RoutingRoutes = RouterModule.forRoot(routes);
