import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-confirmation-dialog',
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss']
})
export class ConfirmationDialogComponent {
  constructor(private router: Router) {

  }

  @Input() userRole = '';
  @Input() message: string = 'Which page do you want to access?';
  @Output() onConfirm = new EventEmitter<boolean>();

  confirm() {
    this.router.navigate(['/users-management']).then();
    this.onConfirm.emit(true);
  }

  cancel() {
    this.router.navigate(['/device-management']).then();
    this.onConfirm.emit(false);
  }

  idDisabled() {
    return this.userRole === 'ADMIN';
  }
}
