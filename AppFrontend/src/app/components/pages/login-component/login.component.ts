import {Component} from '@angular/core';
import {AuthenticationService} from '../../../service/authentication.service';
import {LoginRequest} from '../../../model/LoginRequest';
import {take} from 'rxjs';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-login-component',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss', '../../common/page-styling/opening-styling.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;

  userRole = '';

  isDisabled = false;

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private toastr: ToastrService,
              private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      username: [''],
      password: ['', Validators.required]
    });
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('admin-page');
  }

  handleLoginUser() {
    let loginRequest = new LoginRequest();
    loginRequest.email = this.loginForm.value.username;
    loginRequest.password = this.loginForm.value.password;

    this.authenticationService.loginUser(loginRequest).pipe(take(1)).subscribe({
      next: (response) => {
        let token = response.token;
        sessionStorage.setItem('token', token);
        this.userRole = this.authenticationService.getUserRole();
        if (this.userRole !== 'ADMIN') {
          this.router.navigate(['/client']).then();
        } else {
          this.isDisabled = !this.isDisabled;
        }
      },
      error: (err) => {
        if (err.status === 500 || err.status === 403 || err.status === 404) {
          this.toastr.error('Wrong email or password! Try again!', 'Error', {
            timeOut: 5000,
            extendedTimeOut: 1000,
            progressBar: true,
            closeButton: true
          });
        }
      }
    });
  }

  navigateToRegister() {
    this.router.navigate(['register']).then();
  }

  selectOption(option: boolean) {
    if (option) {
      sessionStorage.setItem('admin-page', 'users');
    } else {
      sessionStorage.setItem('admin-page', 'device');
    }
  }
}

