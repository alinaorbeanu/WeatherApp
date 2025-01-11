import {ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {UserService} from '../../../service/user.service';
import {User} from '../../../model/User';
import {take} from 'rxjs';
import {Message} from '../../../model/Message';
import {AuthenticationService} from '../../../service/authentication.service';
import {ChatService} from '../../../service/chat.service';
import {SessionChat} from '../../../model/SessionChat';
import {SeenRequest} from '../../../model/SeenRequest';
import {FrameImpl, Stomp} from '@stomp/stompjs';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-chat-grid',
  templateUrl: './chat-grid.component.html',
  styleUrls: ['./chat-grid.component.scss']
})
export class ChatGridComponent implements OnChanges, OnInit {

  @Input() id: number = 0;
  @Output() lastMessage: EventEmitter<any> = new EventEmitter();
  @Output() messageWasSent: EventEmitter<any> = new EventEmitter();
  messageSent: boolean = false;
  selectedUser: User = new User();
  selectedUserName: string = '';
  currentUserEmail: string = '';
  messages: Message[] = [];
  content!: SessionChat;
  isClient: boolean = false;
  newMessage: string = '';
  stompChat: any;
  stompTyping: any;
  stillTyping: number = 0;

  constructor(private userService: UserService,
              private chatService: ChatService,
              private toastr: ToastrService,
              private authService: AuthenticationService,
              private changeDetectorRef: ChangeDetectorRef) {
    this.currentUserEmail = this.authService.getUsername();
  }

  ngOnInit() {
    this.stompChat = Stomp.over(() => new WebSocket('ws://localhost:8086/api/websocket'));
    let that = this;
    this.stompChat.connect({}, function () {
      that.getSelectedUser();
      that.stompChat.subscribe('/topic', function (message: FrameImpl) {
        let webSocketMsg = JSON.parse(message.body);
        that.getSelectedUser();
        if (that.authService.getUsername() === webSocketMsg.userName) {
          if (webSocketMsg.sender === that.content.client || webSocketMsg.sender === that.content.admin) {
            let message = new Message(webSocketMsg.message, webSocketMsg.sender, webSocketMsg.userName, 'unseen', that.content.id.toString());
            that.messages.push(message);
          }
        }
      })
    });

    this.stompTyping = Stomp.over(() => new WebSocket('ws://localhost:8086/api/websocket'));

    this.stompTyping.connect({}, function () {
      that.stompTyping.subscribe('/topic2', function (message: FrameImpl) {
        let webSocketMsg = JSON.parse(message.body);
        if (that.authService.getUsername() === webSocketMsg.userName) {
          that.toastr.info(webSocketMsg.sender + webSocketMsg.message, 'Messenger', {
            timeOut: 5000,
            extendedTimeOut: 1000,
            progressBar: true,
            closeButton: true
          });
        }
      })
    });
  }

  ngOnChanges() {
    this.changeDetectorRef.detectChanges();
    this.getSelectedUser();
  }

  getSelectedUser() {
    if (this.id === 0) {
      this.selectedUserName = '';
    } else {
      this.userService.findUserById(this.id).pipe(take(1)).subscribe({
        next: (response) => {
          this.selectedUser = response;
          this.selectedUserName = this.selectedUser.name;
          this.getMessages(this.selectedUser.email);
        }
      })
    }
  }

  getMessages(selectedUser: string) {
    const currentUser = this.authService.getUsername();

    const receiver = this.isClient ? currentUser : selectedUser;
    const sender = this.isClient ? selectedUser : currentUser;

    this.chatService.findAllByReceiverAndSenderAndStatus(receiver, sender).pipe(take(1)).subscribe({
      next: (response) => {
        if (response != null) {
          this.content = response;
          this.messages = this.content.messages;
          this.setSeenOnMessages();
        }
      }
    });
  }

  setSeenOnMessages() {
    let seenRequest = new SeenRequest(this.content.id, this.selectedUser.email);
    this.chatService.setOnSeenMessage(seenRequest).pipe(take(1)).subscribe({
      next: () => {
      }
    })
  }

  sendMessage() {
    if (this.newMessage != '') {
      let receiver = this.selectedUser.email;
      let message = new Message(this.newMessage.toString(), this.currentUserEmail, receiver, 'unseen', this.content.id.toString());

      this.chatService.sendMessage(message).pipe(take(1)).subscribe({
        next: () => {
          this.messageSent = !this.messageSent;
          this.messageWasSent.emit(this.messageSent);
          this.messages.push(message);
          this.newMessage = '';
          this.stillTyping = 0;
        }
      });
    }
  }

  isTyping() {
    let receiver = this.selectedUserName;
    let message = new Message(' is typing', this.authService.getUsername(), receiver, '', this.content.id.toString());

    if (this.newMessage === '') {
      this.stillTyping = 0;
    }

    if (this.stillTyping < 1 && this.newMessage !== '') {
      this.chatService.isTypingNotification(message).pipe(take(1)).subscribe({
        next: () => {
          this.stillTyping++;
        }
      })
    }
  }
}
