import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.scss']
})
export class UserCardComponent {

  @Input() id: number = 0;
  @Input() name: string = '';
  @Input() email: string = '';
  @Input() role: string = '';
  @Output() deleteUserEmitter = new EventEmitter();

  constructor(private router: Router) {
  }

  redirectToEditUser() {
    const encodedId = btoa(this.id.toString());
    this.router.navigate(['/edit-user/' + encodedId]).then();
  }

  deleteUser() {
    this.deleteUserEmitter.emit(true);
  }
}
