import {Component, Input} from '@angular/core';
import {Prediction} from '../../../model/Prediction';

@Component({
  selector: 'app-notification-train',
  templateUrl: './notification-train.component.html',
  styleUrls: ['./notification-train.component.scss']
})
export class NotificationTrainComponent {

  @Input() isDisabled!: boolean;
  @Input() prediction: Prediction[] = [];
  isInformationCard: boolean = false;

  confirm() {
    this.isDisabled = false;
    this.isInformationCard = true;
    console.log(this.prediction)
  }

  cancel() {
    this.isDisabled = false;
  }

  closeWindow() {
    this.isInformationCard = false;
  }
}
