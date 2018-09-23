import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import * as moment from 'moment';

import { CategoryModel } from '../category/category-model';
import { WorkoutModel } from '../workout/workout-model';
import { ServiceBase } from '../_base/base-service';
import { DateTimeService } from './date-time.service';

@Injectable({
  providedIn: 'root'
})
export class WorkoutService extends ServiceBase {

  private  workoutUrl: String = this.baseUrl + '/workout';

  constructor(private client: HttpClient) {
    super(client);
  }

  public static clone(workout: WorkoutModel): WorkoutModel {
    const category = new CategoryModel(workout.category.category, false, workout.category.id);
    return new WorkoutModel(workout.title, category, workout.note, workout.burnrate, false,
      workout.start, workout.end, workout.id);
  }

  public static cloneAll(workouts: WorkoutModel[]): WorkoutModel[] {
    const output: WorkoutModel[] = [];
    Array.from(workouts).forEach((workout: WorkoutModel) => {
      output.push(WorkoutService.clone(workout));
    });
    return output;
  }

  public static copy(existing: WorkoutModel, updated: WorkoutModel): void {
    if (updated) {
      existing.editing = false;
      existing.id = updated.id;
      existing.end = updated.end;
      existing.note = updated.note;
      existing.title = updated.title;
      existing.start = updated.start;
      existing.burnrate = updated.burnrate;
      existing.notes = WorkoutService.createNotes(updated.note, updated.start, updated.end);
      if (updated.category) {
        existing.category.id = updated.category.id;
        existing.category.category = updated.category.category;
        existing.category.editing = false;
      }
    }
  }

  public static createNotes(note: string, start: Date, end: Date): string[] {
    const notes: string[] = [];
    if (note) { notes.push(note); }
    if (start) { notes.push(WorkoutService.getStartedAt(start)); }
    if (end) { notes.push(WorkoutService.getCompletedAt(end)); }
    return notes;
  }

  public static getCompletedAt(date: Date): string {
    const format = moment(date).isBefore(moment().startOf('day')) ?
    DateTimeService.getFormatForTimestamp() : DateTimeService.getFormatForTime();
    return `Completed@ ${moment(date).format(format)}`;
  }

  public static getStartedAt(date: Date): string {
    const format = moment(date).isBefore(moment().startOf('day')) ?
    DateTimeService.getFormatForTimestamp() : DateTimeService.getFormatForTime();
    return `Started@ ${moment(date).format(format)}`;
  }

  public create(workout: WorkoutModel, authToken: string) {
    return this.client.post(
      this.workoutUrl.toString(),
      JSON.stringify(workout),
      {
        headers: {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  public delete(id: number, authToken: string) {
    return this.client.delete(
      `${this.workoutUrl.toString()}/${id}`,
      {
        headers: {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  public getAll(authToken: string) {
    return this.client.get<WorkoutModel[]>(
      this.workoutUrl.toString(),
      {
        headers: {
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  public search(start: Date, end: Date, authToken: string) {
    const from = DateTimeService.format(start, DateTimeService.getISODateFormat());
    const to   = DateTimeService.format(  end, DateTimeService.getISODateFormat());
    return this.client.get<WorkoutModel[]>(
      `${this.workoutUrl}/start/${from}/end/${to}`,
      {
        headers: {
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

  public update(workout: WorkoutModel, authToken: string) {
    return this.client.put<WorkoutModel>(
      `${this.workoutUrl.toString()}/${workout.id}`,
      JSON.stringify(workout),
      {
        headers: {
          'Content-Type' : 'application/json',
          'Accept' : 'application/json',
          'Authorization' : authToken
        },
        observe: 'response',
        reportProgress: false,
        responseType: 'json'
      }
    );
  }

}
