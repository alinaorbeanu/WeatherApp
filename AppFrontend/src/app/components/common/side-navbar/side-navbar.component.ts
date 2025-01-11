import {Component, Input} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-side-navbar',
  templateUrl: './side-navbar.component.html',
  styleUrls: ['./side-navbar.component.scss']
})
export class SideNavbarComponent {

  isOpened = false;
  isUsersPage!: boolean;
  @Input() isAdmin = false;
  isAddUserPressed = false;
  isAddDevicePressed = false;

  constructor(private router: Router) {
    if (sessionStorage.getItem('admin-page') === null) {
      this.isUsersPage = false;
    } else {
      this.isUsersPage = !sessionStorage.getItem('admin-page')!.includes('device');
    }
  }

  displaySideMenu() {
    return this.isOpened = !this.isOpened;
  }

  navigateToDevice() {
    sessionStorage.setItem('admin-page', 'device');
    this.isUsersPage = false;
    this.router.navigate(['/device-management']).then();
  }

  navigateToUser() {
    sessionStorage.setItem('admin-page', 'users');
    this.isUsersPage = true;
    this.router.navigate(['/users-management']).then();
  }

  pressAddUser() {
    this.isAddUserPressed = !this.isAddUserPressed;
    if (this.isAddUserPressed) {
      this.router.navigate(['/add-user']).then();
    } else {
      this.router.navigate(['/users-management']).then();
    }
  }

  pressAddDevice() {
    this.isAddDevicePressed = !this.isAddDevicePressed;
    if (this.isAddDevicePressed) {
      this.router.navigate(['/add-device']).then();
    } else {
      this.router.navigate(['/device-management']).then();
    }
  }

  navigateToMyDevice() {
    this.router.navigate(['/client']).then();
  }
}
