import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-chat-card',
  templateUrl: './chat-card.component.html',
  styleUrls: ['./chat-card.component.scss']
})
export class ChatCardComponent {

  @Input() id: number = 0;
  @Input() name: string = '';
  @Input() isCreateSession: boolean = false;
  @Output() isCreateSessionEmitter = new EventEmitter();
  @Output() userSelected = new EventEmitter();
  @Input() userSelectedEmail: string = '';
  @Input() messageWasSent: boolean = false;


  isCreateNewSessionPressed() {
    this.isCreateSessionEmitter.emit(true);
  }

  openChatFromBroadcast(id: number) {
    this.userSelected.emit(id);
  }
}
