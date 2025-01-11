import {Component, OnInit} from '@angular/core';
import {Device} from '../../../model/Device';
import {take} from 'rxjs';
import {UserDeviceMappingService} from '../../../service/user-device-mapping.service';
import {AuthenticationService} from '../../../service/authentication.service';
import {ChatService} from '../../../service/chat.service';


@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})
export class ClientComponent implements OnInit {

  devices: Device[] = [];
  private currentUser = '';
  notificationCount: number = 0;

  constructor(private authService: AuthenticationService,
              private chatService: ChatService,
              private userDeviceMappingService: UserDeviceMappingService) {
  }


  ngOnInit() {
    this.loadDevices();
    this.currentUser = this.authService.getUsername();
    this.getNotificationNumber();
  }

  loadDevices() {
    this.userDeviceMappingService.findAllUserDevices().pipe(take(1)).subscribe({
      next: (devices) => {
        this.devices = devices;
      }
    });
  }

  getNotificationNumber() {
    this.chatService.findAllOpenedChatsByUsernameAndStatus(this.currentUser).pipe(take(1)).subscribe({
      next: (response) => {
        if (response.length === 0) {
          return;
        }
        this.chatService.findAllUnreadMessagesByChatsAndUsername(response, this.currentUser).pipe(take(1)).subscribe({
          next: (newResponse) => {
            this.notificationCount = newResponse.length;
          }
        })
      }
    });
  }
}
