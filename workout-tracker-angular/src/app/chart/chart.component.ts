import { Component, ViewChild, OnDestroy } from '@angular/core';
import * as moment from 'moment';
import { BsDaterangepickerDirective, BsDaterangepickerConfig } from 'ngx-bootstrap/datepicker';
import { api, Chart } from 'taucharts';
import 'taucharts/dist/plugins/crosshair';
import 'taucharts/dist/plugins/legend';
import 'taucharts/dist/plugins/tooltip';

import { DateTimeService } from '../_services/date-time.service';
import { WorkoutService } from '../_services/workout.service';
import { AuthService } from '../_services/auth.service';
import { WorkoutModel } from '../workout/workout-model';
import { ChartModel } from './chart-model';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnDestroy {

  public daterange: Date[] = [];

  @ViewChild(BsDaterangepickerDirective)
  public daterangePicker: BsDaterangepickerDirective;

  // Date Picker Custom Configuration
  public daterangePickerConfig: Partial<BsDaterangepickerConfig> = {
    containerClass: 'theme-dark-blue',
    displayMonths: 2,
    showWeekNumbers: false
  };

  private chartArea: Chart;

  constructor(
    private authService: AuthService,
    private restService: WorkoutService
  ) { }

  ngOnDestroy() { this.destroyChart(); }

  private createChart(data: ChartModel[]) {
    // Setup a plotting area for chart
    this.chartArea = new Chart({
      data: data,
      label: 'Calorie Consumption Chart',
      type: 'stacked-bar',
      x: 'date',
      y: 'burntCalories',
      color: 'category',
      size: 'burntCalories',
      guide: {
        showGridLines: 'y',
        x: { nice: false, label: { padding: 0, text: 'Date Range' } },
        y: { nice: false, label: { padding: 0, text: 'Total Calories Burnt' } },
      },
      settings: {
        asyncRendering: true,
        autoRatio: true,
        excludeNull: true,
      },
      plugins: [
        api.plugins.get('crosshair')({ xAxis: false, yAxis: true }),
        api.plugins.get('legend')   ({ position: 'left' }),
        api.plugins.get('tooltip')({
          fields: ['date', 'category', 'title', 'burntCalories'],
          formatters: {
                     date: {label: 'Date',           format: (value: string) => value.valueOf()},
                 category: {label: 'Category',       format: (value: string) => value.valueOf()},
                    title: {label: 'Workout',        format: (value: string) => value.valueOf()},
            burntCalories: {label: 'Calories Burnt', format: (value: number) => value.toFixed(1)},
          }
        }),
      ]
    });
    this.chartArea.renderTo('#chart-area');
    this.chartArea.refresh();
  }

  private destroyChart() {
    if (this.chartArea) { this.chartArea.destroy(); }
  }

  /* @HostListener('window: scroll')
  onscrollEvent() { this.daterangePicker.hide(); } */
  public refreshChart(daterange: Date[]) {

    this.destroyChart();
    this.daterange.length = 0;

    const from = moment(moment(daterange[0]).format(DateTimeService.getISODateFormat())).toDate();
    const to = moment(moment(daterange[1]).format(DateTimeService.getISODateFormat())).toDate();
    console.log(`${from}\n${to}`);

    this.restService.search(from, to, this.authService.auth).subscribe(
      res => {
        if (res.status === 200 && res.body) {
          const workouts = WorkoutService.cloneAll(res.body);
          const chartData = this.prepareChartData(workouts);
          if (chartData.length > 0) {
            this.createChart(chartData);
            this.daterange.push(from);
            this.daterange.push(to);
          }
        } else if (res.status === 204) {
          console.log('No workouts found');
        }
      },
      err => { },
       () => { }
    );
  }

  public refreshForPrevWeek() {
    const from = moment().startOf('week').subtract(7, 'd').toDate();
    const to = moment().endOf('week').subtract(7, 'd').toDate();
    this.refreshChart(Array.of(from, to));
  }

  public refreshForThisWeek() {
    const from = moment().startOf('week').toDate();
    const to = moment().endOf('week').toDate();
    this.refreshChart(Array.of(from, to));
  }

  public refreshForToday() {
    const from = DateTimeService.getTodayAtMidnight();
    const to = DateTimeService.getTodayAtMidnight();
    this.refreshChart(Array.of(from, to));
  }

  public refreshForYesterday() {
    const from = DateTimeService.getYesterdayAtMidnight();
    const to = DateTimeService.getYesterdayAtMidnight();
    this.refreshChart(Array.of(from, to));
  }

  private prepareChartData(workouts: WorkoutModel[]) {
    const chartData: ChartModel[] = [];
    workouts.forEach(workout =>
      chartData.push(new ChartModel(workout))
    );
    return chartData;
  }

}
