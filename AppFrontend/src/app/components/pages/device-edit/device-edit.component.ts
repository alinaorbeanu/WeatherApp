import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../../../model/User';
import {UserService} from '../../../service/user.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {take} from 'rxjs';
import {Device} from '../../../model/Device';
import {DeviceService} from '../../../service/device.service';

@Component({
  selector: 'app-device-edit',
  templateUrl: './device-edit.component.html',
  styleUrls: ['../../common/page-styling/admin-actions.component.scss']
})
export class DeviceEditComponent implements OnInit {
  users: User[] = [];
  deviceGroup!: FormGroup;
  currentDevice: Device = new Device();

  constructor(private userService: UserService,
              private deviceService: DeviceService,
              private formBuilder: FormBuilder,
              private router: Router,
              private toastr: ToastrService) {
  }

  ngOnInit() {
    this.deviceGroup = this.formBuilder.group({
      description: ['', [Validators.required]],
      address: ['', [Validators.required]],
      deviceNumber: ['', [Validators.required]],
      maximumEnergyProduction: ['', [Validators.required]],
      userSelected: ['', [Validators.required]],
      userIdSelected: ['', [Validators.required]],
    });
    this.getCurrentDevice();
    this.loadUsers();
  }

  loadUsers() {
    this.userService.findAllUsers().pipe(take(1)).subscribe({
      next: (users) => {
        this.users = users;
      }
    });
  }

  updateDevice() {
    let device = new Device();

    device.id = this.currentDevice.id;
    device.description = this.deviceGroup.get('description')?.value;
    device.address = this.deviceGroup.get('address')?.value;
    device.deviceNumber = this.deviceGroup.get('deviceNumber')?.value;
    device.maximumEnergyProduction = this.deviceGroup.get('maximumEnergyProduction')?.value;
    device.ownerId = this.deviceGroup.get('userIdSelected')?.value;
    if (device.owner === '-') {
      device.owner = '0';
    }

    this.deviceService.updateDevice(device).pipe(take(1)).subscribe({
      next: (device) => {
        this.toastr.success('Device with address: ' + device.address + ' was updated!', 'Success', {
          timeOut: 5000,
          extendedTimeOut: 1000,
          progressBar: true,
          closeButton: true
        });
        this.router.navigate(['/device-management']).then();
      }
    });
  }

  getCurrentDevice() {
    let deviceId = +(atob(decodeURIComponent(this.router.url.split('/')[2])));
    this.deviceService.findDeviceById(deviceId).pipe(take(1)).subscribe({
      next: (response: any) => {
        this.currentDevice = response;
        let owner = '-';
        let userId = '0';
        if (this.currentDevice.owner !== null) {
          owner = this.currentDevice.owner;
          userId = this.currentDevice.ownerId;
        }
        this.deviceGroup.patchValue({
          description: this.currentDevice.description,
          address: this.currentDevice.address,
          deviceNumber: this.currentDevice.deviceNumber,
          maximumEnergyProduction: this.currentDevice.maximumEnergyProduction,
          userSelected: owner,
          userIdSelected: userId
        });
      },
      error: () => {
      }
    });
  }

  cancelEdit() {
    this.router.navigate(['/device-management']).then();
  }
}
