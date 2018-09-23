import * as moment from 'moment';

import { WorkoutModel } from '../workout/workout-model';
import { DateTimeService } from '../_services/date-time.service';

export class ChartModel {
    public burntCalories = 0.0;
    public category: string;
    public date: string;
    public duration = 0.0;
    public title: string;
    constructor(public workout: WorkoutModel) {
        if (workout) {
            this.duration = DateTimeService.getDiffInMins(workout.start, workout.end);
            this.burntCalories = workout.burnrate * this.duration;
            this.category = workout.category.category;
            this.date = DateTimeService.format(workout.start, DateTimeService.getISODateFormat());
            this.title = workout.title;
        }
    }
}
