import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-calendar-card',
  templateUrl: './calendar-card.component.html',
  styleUrls: ['./calendar-card.component.scss']
})
export class CalendarCardComponent implements OnInit {

  startDate!: Date;
  endDate!: Date;
  minDate!: Date;
  maxDate!: Date;
  @Input() isCalendarPressed: boolean = false;
  @Output() selectedDatesEmitted = new EventEmitter();
  @Output() selectedEndDate = new EventEmitter();

  ngOnInit(): void {
    this.startDate = new Date(2022, 9, 30);
    this.endDate = new Date(2023, 9, 26);
    this.minDate = new Date(2022, 9, 30);
    this.maxDate = new Date(2023, 9, 26);
  }

  handlePredict() {
    this.selectedDatesEmitted.emit(this.startDate + '-' + this.endDate);
  }
}
