import { Injectable } from '@angular/core';
import * as moment from 'moment';

@Injectable({
  providedIn: 'root'
})
export class DateTimeService {

  constructor() { }

  public static format(input: Date, format: string): string {
    return moment(input).format(format);
  }

  private static getDiffIn(start: Date, end: Date): moment.Duration {
    return moment.duration(moment(end).diff(moment(start)));
  }

  public static getDiffInHours(start: Date, end: Date): number {
    return DateTimeService.getDiffIn(start, end).as('hours');
  }

  public static getDiffInMillis(start: Date, end: Date): number {
    return DateTimeService.getDiffIn(start, end).as('milliseconds');
  }

  public static getDiffInMins(start: Date, end: Date): number {
    return DateTimeService.getDiffIn(start, end).as('minutes');
  }

  public static getDiffInSeconds(start: Date, end: Date): number {
    return DateTimeService.getDiffIn(start, end).as('seconds');
  }

  public static getFormatForDate(): string {
    return 'DD MMM YYYY';
  }

  public static getFormatForTime(): string {
    return DateTimeService.getISOTimeFormat();
  }

  public static getFormatForTimestamp(): string {
    return DateTimeService.getFormatForDate() + ' ' + DateTimeService.getFormatForTime();
  }

  public static getFormatForTimestampWithZone(): string {
    return DateTimeService.getFormatForTimestamp() + ' ' + DateTimeService.getTimeZoneFormat();
  }

  public static getISODateFormat(): string {
    return 'YYYY-MM-DD';
  }

  public static getISOTimeFormat(): string {
    return 'HH:mm:ss';
  }

  public static getISOTimestampFormat(): string {
    return DateTimeService.getISODateFormat() + 'T' + DateTimeService.getISOTimeFormat();
  }

  public static getISOTimestampFormatWithZone(): string {
    return DateTimeService.getISOTimestampFormat() + DateTimeService.getTimeZoneFormat();
  }

  public static getTimeZoneFormat(): string {
    return 'Z';
  }

  public static getUnixTimestampFormat(): string {
    return 'X';
  }

  public static getTodayAtMidnight(): Date {
    return moment(moment().format(DateTimeService.getISODateFormat())).toDate();
  }

  public static getYesterdayAtMidnight(): Date {
    return moment(moment().format(DateTimeService.getISODateFormat())).subtract(1, 'd').toDate();
  }

}
