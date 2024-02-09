import {formatDate} from "@angular/common";

export function formatToDateString(date: Date, locale: string = 'de-DE'): string {
  return formatDate(date, 'yyyy-MM-dd', locale);
}
