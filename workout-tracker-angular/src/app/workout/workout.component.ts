import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';

import { AuthService } from '../_services/auth.service';
import { WorkoutService } from '../_services/workout.service';
import { WorkoutModel } from './workout-model';
import { WorkoutPlusComponent } from '../workout-plus/workout-plus.component';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.css']
})
export class WorkoutComponent implements OnInit {

  private workouts: WorkoutModel[] = [];
  filteredWorkouts: WorkoutModel[] = [];
  filterQuery$ = new Subject<string>();

  constructor(
    private authService: AuthService,
    private restService: WorkoutService,
    private modalService: NgbModal,
  ) { }

  ngOnInit() {
    this.retrieveAll(this.authService.auth);
    this.filterQuery$.pipe(
      debounceTime(400), distinctUntilChanged(),
      map(filterQuery => this.filter(filterQuery))
    ).subscribe();
  }

  private add(workout: WorkoutModel) {
    if (workout) {
      workout.notes = null;
      this.restService.create(workout, this.authService.auth).subscribe(
        res => {
          const resource = document.createElement('a');
          resource.href = res.headers.get('Location');
          const pathItems = resource.pathname.split('/');
          const id = pathItems[pathItems.length - 1];

          workout.id = parseInt(id, 10);
          this.workouts.push(WorkoutService.clone(workout));
        },
        err => {
          if (err.status === 401 || err.status === 403) {
            console.log('Unauthorized access to Workout');
          } else if (err.status === 400 && err.error) {
            alert(Array.from(err.error.messages).join('\n'));
          }
        },
        () => { this.resetFilteredWorkouts(); }
      );
    } else {
      console.log('Invalid workout name :: ' + workout);
    }
  }

  protected delete(workout: WorkoutModel) {
    const confirmed = confirm('Do you really want to delete this workout?');
    if (confirmed) {
      this.restService.delete(workout.id, this.authService.auth).subscribe(
        res => {
          if (res.status === 200) {
            console.log(`Workout #${workout.id} deleted successfully`);
            this.workouts.splice(this.workouts.indexOf(workout), 1);
          } else {
            console.log(`Workout [#${workout.title}] could not be deleted`);
          }
        },
        err => {
          if (err.status === 401 || err.status === 403) {
            console.log('Unauthorized access to workout');
          } else if (err.status === 400) {
            console.log('Non existing workout');
          }
          alert('Error deleting workout');
        },
        () => this.resetFilteredWorkouts()
      );
    }
  }

  private edit(workout: WorkoutModel) {
    workout.editing = true;
    this.update(workout);
  }

  protected end(workout: WorkoutModel) {
    workout.end = new Date();
    // workout.addNote(WorkoutService.getCompletedAt(workout.end));
    this.update(workout);
  }

  protected filter(filterQuery: String): void {
    this.filteredWorkouts.length = 0;
    this.workouts.forEach(
      (workout: WorkoutModel) => {
        const query = filterQuery ? filterQuery.trim().toLocaleLowerCase() : '';
        const title = workout.title ? workout.title.trim().toLocaleLowerCase() : '';
        if (title.indexOf(query) !== -1) {
          this.filteredWorkouts.push(workout);
        }
      }
    );
  }

  public openTemplate(action: string, workout?: WorkoutModel): void {
    const modalRef = this.modalService.open(WorkoutPlusComponent, { centered: true });
    modalRef.componentInstance.action = action;
    if (workout) { modalRef.componentInstance.workout = workout; }
    modalRef.result.then(
      output => {
        if (action === 'Add') {
          this.add(output);
        } else if (action === 'Update') {
          this.edit(output);
        }
      },
      dismiss => {}
    );
  }

  private resetFilteredWorkouts() {
    this.filteredWorkouts.length = 0;
    this.workouts.forEach(workout => {
      this.filteredWorkouts.push(workout);
    });
  }

  private retrieveAll(authToken: string): void {
    this.restService.getAll(authToken).subscribe(
      res => {
        if (res.status === 200 && res.body) {
          this.workouts = WorkoutService.cloneAll(res.body);
          /* Array.from(res.body).forEach((workout: WorkoutModel) => {
            this.workouts.push(WorkoutService.clone(workout));
          }); */
        } else if (res.status === 204) {
          console.log('No workouts found');
          this.workouts.length = 0;
        }
      },
      err => {
        if (err.status === 401 || err.status === 403) {
          console.log('Unauthorized access to workout');
          this.workouts.length = 0;
        }
        alert('Error accessing workouts');
      },
      () => this.resetFilteredWorkouts()
    );
  }

  protected start(workout: WorkoutModel) {
    workout.start = new Date();
    // workout.addNote(WorkoutService.getStartedAt(workout.start));
    this.update(workout);
  }

  private update(workout: WorkoutModel) {
    if (workout && workout.title && workout.category && workout.category.category) {
      this.restService.update(workout, this.authService.auth).subscribe(
        res => {
          if (res.status === 200) {
            console.log(`Workout #${workout.id} updated successfully`);
            // this.workouts.splice(this.workouts.indexOf(workout), 1, WorkoutService.clone(res.body));
            WorkoutService.copy(workout, res.body);
          } else if (res.status === 204) {
            alert('Workout is not available in database');
          } else {
            console.log(`Workout #${workout.id} could not be updated`);
          }
        },
        err => {
          if (err.status === 401 || err.status === 403) {
            console.log('Unauthorized access to workouts');
          }
          alert(`Error updating workout #${workout.id}`);
          if (err.error) {
            err.error.messages.forEach(error => {
              console.log(error);
            });
          }
        },
        () => workout.editing = false
      );
    }
  }

}
