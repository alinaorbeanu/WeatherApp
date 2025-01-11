import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {User} from '../../../model/User';
import {Role} from '../../../model/Role';
import {take} from 'rxjs';
import {UserService} from '../../../service/user.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['../../common/page-styling/admin-actions.component.scss']
})
export class AddUserComponent implements OnInit {

  userGroup!: FormGroup;

  constructor(private userService: UserService,
              private formBuilder: FormBuilder,
              private router: Router,
              private toastr: ToastrService) {
  }

  ngOnInit() {
    this.userGroup = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required]],
      password: ['', [Validators.required]],
      role: ['', [Validators.required]]
    });
  }

  addUser() {
    let newUser = new User();
    let userRole = new Role();
    newUser.name = this.userGroup.get('name')?.value;
    newUser.email = this.userGroup.get('email')?.value;
    newUser.password = this.userGroup.get('password')?.value;
    userRole.name = this.userGroup.get('role')?.value;
    switch (userRole.name) {
      case('ADMIN'):
        userRole.id = 1;
        break;
      case('CLIENT') :
        userRole.id = 2;
        break;
    }
    newUser.roleDTO = userRole;

    this.userService.addUser(newUser).pipe(take(1)).subscribe({
      next: (user) => {
        this.toastr.success('User with name: ' + user.name + ' was created!', 'Success', {
          timeOut: 5000,
          extendedTimeOut: 1000,
          progressBar: true,
          closeButton: true
        });
        this.userGroup.patchValue({
          name: '',
          email: '',
          password: '',
          role: ''
        });
        this.router.navigate(['/users-management']).then();
      }
    });
  }

  cancelEdit() {
    this.router.navigate(['/users-management']).then();
  }
}
