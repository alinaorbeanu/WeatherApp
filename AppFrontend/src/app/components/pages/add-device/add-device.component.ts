import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Device} from '../../../model/Device';
import {take} from 'rxjs';
import {DeviceService} from '../../../service/device.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-device',
  templateUrl: './add-device.component.html',
  styleUrls: ['../../common/page-styling/admin-actions.component.scss']
})
export class AddDeviceComponent implements OnInit {

  deviceGroup!: FormGroup;

  constructor(private deviceService: DeviceService,
              private formBuilder: FormBuilder,
              private toastr: ToastrService,
              private router: Router) {
  }

  ngOnInit() {
    this.deviceGroup = this.formBuilder.group({
      description: ['', [Validators.required]],
      address: ['', [Validators.required]],
      deviceNumber: ['', [Validators.required]],
      maximumEnergyProduction: ['', [Validators.required]]
    });
  }


  addDevice() {
    let newDevice = new Device();
    newDevice.description = this.deviceGroup.get('description')?.value;
    newDevice.address = this.deviceGroup.get('address')?.value;
    newDevice.maximumEnergyProduction = this.deviceGroup.get('maximumEnergyProduction')?.value;
    newDevice.deviceNumber = this.deviceGroup.get('deviceNumber')?.value;

    this.deviceService.addDevice(newDevice).pipe(take(1)).subscribe({
      next: (device) => {
        this.toastr.success('Device from address: ' + device.address + ' was created!', 'Success', {
          timeOut: 5000,
          extendedTimeOut: 1000,
          progressBar: true,
          closeButton: true
        });
        this.router.navigate(['/device-management']).then();
      }
    });
  }

  cancelAdd() {
    this.router.navigate(['/device-management']).then();
  }
}
