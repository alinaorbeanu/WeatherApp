import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Prediction} from '../../../model/Prediction';

@Component({
  selector: 'app-information-card',
  templateUrl: './information-card.component.html',
  styleUrls: ['./information-card.component.scss']
})
export class InformationCardComponent {

  @Input() prediction: Prediction[] = [];
  @Output() closeWindow = new EventEmitter();

  close() {
    this.closeWindow.emit(true);
  }
}
