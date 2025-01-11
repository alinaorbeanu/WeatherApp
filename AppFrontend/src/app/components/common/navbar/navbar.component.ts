import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {AuthenticationService} from '../../../service/authentication.service';
import {UserService} from '../../../service/user.service';
import {take} from 'rxjs';
import {FrameImpl, Stomp} from '@stomp/stompjs';
import {User} from '../../../model/User';
import {ChatService} from '../../../service/chat.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  @Input() title = '';
  @Input() showChat: boolean = true;
  actualUser: User = new User();
  isChatButtonPressed: boolean = false;
  notificationCount: number = 0;
  userName: string = '';
  stompChat: any;
  stompMessage: any;


  constructor(private router: Router,
              private authService: AuthenticationService,
              private chatService: ChatService,
              private userService: UserService,
              private toastr: ToastrService,
              private location: Location) {
  }

  ngOnInit() {
    this.actualUser = this.getUserByEmail();
    let that = this;

    this.stompChat = Stomp.over(() => new WebSocket('ws://localhost:8086/api/websocket'));

    this.stompChat.connect({}, function () {
      that.stompChat.subscribe('/topic', function (message: FrameImpl) {
        let webSocketMsg = JSON.parse(message.body);
        if (that.authService.getUsername() === webSocketMsg.userName) {
          that.notificationCount++;
          that.toastr.info(webSocketMsg.sender + ': ' + webSocketMsg.message, 'Messenger', {
            timeOut: 5000,
            extendedTimeOut: 1000,
            progressBar: true,
            closeButton: true
          });
        }
      })
    });

    this.stompMessage = Stomp.over(() => new WebSocket('ws://localhost:8086/api/websocket'));

    this.stompMessage.connect({}, function () {
      that.stompMessage.subscribe('/topic3', function (message: FrameImpl) {
        that.getNotificationNumber();
      })
    });
  }

  getUserByEmail(): any {
    let username = this.authService.getUsername();
    this.userService.findUserByEmail(username).pipe(take(1)).subscribe({
      next: (result) => {
        this.actualUser = result;
        this.userName = this.actualUser.name;
        this.getNotificationNumber();
      }
    });
  }

  getNotificationNumber() {
    this.chatService.findAllOpenedChatsByUsernameAndStatus(this.actualUser.email).pipe(take(1)).subscribe({
      next: (response) => {
        if (response.length > 0) {
          this.chatService.findAllUnreadMessagesByChatsAndUsername(response, this.actualUser.email).pipe(take(1)).subscribe({
            next: (newResponse) => {
              this.notificationCount = newResponse.length;
            }
          })
        }
      }
    });
  }

  openChat() {
    this.isChatButtonPressed = !this.isChatButtonPressed;
    if (this.isChatButtonPressed) {
      this.router.navigate(['/chat']).then();
    } else {
      this.location.back();
    }
  }

  logout() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('admin-page');
    this.router.navigate(['login']).then();
  }
}
