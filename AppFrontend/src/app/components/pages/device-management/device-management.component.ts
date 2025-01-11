import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../service/user.service';
import {DeviceService} from '../../../service/device.service';
import {AuthenticationService} from '../../../service/authentication.service';
import {Device} from '../../../model/Device';
import {take} from 'rxjs';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../../../model/User';
import {ToastrService} from 'ngx-toastr';
import {FrameImpl, Stomp} from '@stomp/stompjs';

@Component({
  selector: 'app-device',
  templateUrl: './device-management.component.html',
  styleUrls: ['./device-management.component.scss']
})
export class DeviceManagementComponent implements OnInit {
  devices: Device[] = [];
  users: User[] = [];
  deviceGroup!: FormGroup;
  stompTyping: any;
  editDeviceGroup!: FormGroup;

  constructor(private userService: UserService,
              private deviceService: DeviceService,
              private formBuilder: FormBuilder,
              private authService: AuthenticationService,
              private toastr: ToastrService) {
  }

  ngOnInit() {
    this.loadDevices();
    this.loadUsers();
    this.deviceGroup = this.formBuilder.group({
      description: ['', [Validators.required]],
      address: ['', [Validators.required]],
      deviceNumber: ['', [Validators.required]],
      maximumEnergyProduction: ['', [Validators.required]]
    });
    this.editDeviceGroup = this.formBuilder.group({
      id: ['', [Validators.required]],
      description: ['', [Validators.required]],
      address: ['', [Validators.required]],
      deviceNumber: ['', [Validators.required]],
      maximumEnergyProduction: ['', [Validators.required]],
      userSelected: ['', [Validators.required]],
      userIdSelected: ['', [Validators.required]],
    });

    //typing

    this.stompTyping = Stomp.over(() => new WebSocket('ws://localhost:8086/api/websocket'));
    let that = this;
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

  loadUsers() {
    this.userService.findAllUsers().pipe(take(1)).subscribe({
      next: (users) => {
        this.users = users;
      }
    });
  }

  loadDevices() {
    this.deviceService.findAllDevices().pipe(take(1)).subscribe({
      next: (devices) => {
        this.devices = devices;
      }
    });
  }
}
