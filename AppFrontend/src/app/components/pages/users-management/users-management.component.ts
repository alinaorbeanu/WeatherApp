import {Component, OnInit} from '@angular/core';
import {User} from '../../../model/User';
import {UserService} from '../../../service/user.service';
import {ToastrService} from 'ngx-toastr';
import {take} from 'rxjs';

@Component({
  selector: 'app-users-management',
  templateUrl: './users-management.component.html',
  styleUrls: ['./users-management.component.scss']
})
export class UsersManagementComponent implements OnInit {
  users: User[] = [];

  constructor(private userService: UserService,
              private toastr: ToastrService) {
  }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.findAllUsers().pipe(take(1)).subscribe({
      next: (users) => {
        this.users = users;
      }
    });
  }

  deleteUser(userId: number) {
    this.userService.deleteUser(userId).pipe(take(1)).subscribe({
      next: () => {
        this.loadUsers();
        this.toastr.success('User was deleted!', 'Success', {
          timeOut: 5000,
          extendedTimeOut: 1000,
          progressBar: true,
          closeButton: true
        });
      }
    });
  }
}
