import {Component, Input} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../../service/authentication.service';

@Component({
  selector: 'app-device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss']
})
export class DeviceCardComponent {
  @Input() id: number = 0;
  @Input() description: string = '';
  @Input() address: string = '';
  @Input() owner: string = '';
  @Input() maximumEnergyProduction: number = 0;
  @Input() deviceNumber: number = 0;
  isAdmin: boolean = false;

  constructor(private router: Router, private authService: AuthenticationService) {
    this.isAdmin = this.authService.getUserRole() === 'ADMIN';
  }

  showDetails() {
    const encodedId = btoa(this.id.toString());
    this.router.navigate(['/details/' + encodedId]).then();
  }

  redirectToEditDevice() {
    const encodedId = btoa(this.id.toString());
    this.router.navigate(['/edit-device/' + encodedId]).then();
  }
}
