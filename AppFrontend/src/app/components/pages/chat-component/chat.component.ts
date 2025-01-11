import {Component, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {AuthenticationService} from '../../../service/authentication.service';
import {ChatService} from '../../../service/chat.service';
import {take} from 'rxjs';
import {SessionChat} from '../../../model/SessionChat';
import {UserChat} from '../../../model/UserChat';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  users!: FormGroup;
  involvedUsers!: UserChat[];
  isClient = false;
  isCreateNewSessionPressed: boolean = false;
  isAnySessionSelected: boolean = true;
  selectedUserIdBroadcast: number = 0;
  messageWasSent: boolean = false;
  currentUser!: string;
  content!: SessionChat;

  constructor(public authService: AuthenticationService,
              private chatService: ChatService) {
  }

  ngOnInit(): void {
    this.currentUser = this.authService.getUsername();
    this.isClient = this.authService.getRole() === 'CLIENT';
    this.getUsersInvolvedInChats();
  }

  getUsersInvolvedInChats() {
    this.chatService.findAllByClientOrAdminAndStatus(this.currentUser).pipe(take(1)).subscribe({
      next: (response) => {
        this.involvedUsers = response;
      }
    })
  }

  createNewChat() {
    let sessionChat = new SessionChat(this.currentUser);
    this.chatService.createNewSession(sessionChat).pipe(take(1)).subscribe({
      next: () => {
        this.involvedUsers = [];
        this.getUsersInvolvedInChats();
      }
    })
  }

  setCreateNewSession(value: boolean) {
    this.isCreateNewSessionPressed = value;
    this.createNewChat();
  }

  getSelectedUserById(id: number) {
    this.selectedUserIdBroadcast = id;
  }

  setMessageWasSent(value: boolean) {
    this.messageWasSent = value;
  }
}
