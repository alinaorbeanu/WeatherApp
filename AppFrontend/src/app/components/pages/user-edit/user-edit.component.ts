import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../../service/user.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {User} from '../../../model/User';
import {Role} from '../../../model/Role';
import {take} from 'rxjs';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['../../common/page-styling/admin-actions.component.scss']
})
export class UserEditComponent implements OnInit {

  userGroup!: FormGroup;
  currentUser: User = new User();

  constructor(private userService: UserService,
              private formBuilder: FormBuilder,
              private router: Router,
              private toastr: ToastrService) {
  }

  ngOnInit() {
    this.userGroup = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required]],
      role: ['', [Validators.required]]
    });
    this.getCurrentUser();
  }

  updateUser() {
    let user = new User();
    let role = new Role();
    role.name = this.userGroup.get('role')?.value;
    switch (role.name) {
      case('ADMIN'):
        role.id = 1;
        break;
      case('CLIENT') :
        role.id = 2;
        break;
    }

    user.id = this.currentUser.id;
    user.name = this.userGroup.get('name')?.value;
    user.email = this.userGroup.get('email')?.value;
    user.password = this.currentUser.password;
    user.roleDTO = role;
    user.role = role.name;

    this.userService.updateUser(user).pipe(take(1)).subscribe({
      next: (user) => {
        this.toastr.success('User with email: ' + user.email + ' was updated!', 'Success', {
          timeOut: 5000,
          extendedTimeOut: 1000,
          progressBar: true,
          closeButton: true
        });
        this.router.navigate(['/users-management']).then();
      }
    });
  }

  getCurrentUser() {
    let userId = +(atob(decodeURIComponent(this.router.url.split('/')[2])));
    this.userService.findUserById(userId).pipe(take(1)).subscribe({
      next: (response: any) => {
        this.currentUser = response;
        this.userGroup.patchValue({
          name: this.currentUser.name,
          email: this.currentUser.email,
          role: this.currentUser.roleDTO.name,
        });
      },
      error: () => {
      }
    });
  }

  cancelEdit() {
    this.router.navigate(['/users-management']).then();
  }

}
