import {Component, OnInit} from '@angular/core';
import {take} from 'rxjs';
import {RegisterRequest} from '../../../model/RegisterRequest';
import {Role} from '../../../model/Role';
import {AuthenticationService} from '../../../service/authentication.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss', '../../common/page-styling/opening-styling.component.scss']
})
export class RegisterComponent implements OnInit {
  registerGroup!: FormGroup;

  constructor(private authenticationService: AuthenticationService,
              private formBuilder: FormBuilder,
              private toastr: ToastrService,
              private router: Router) {
  }

  ngOnInit() {
    this.registerGroup = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
    this.isButtonDisabled();
  }

  handleRegisterUser() {
    let roleForRequest = new Role();
    roleForRequest.id = 2;
    roleForRequest.name = 'CLIENT';
    let registerRequest = new RegisterRequest();
    registerRequest.name = this.registerGroup.get('name')?.value;
    registerRequest.email = this.registerGroup.get('email')?.value;
    registerRequest.password = this.registerGroup.get('password')?.value;
    registerRequest.roleDTO = roleForRequest;

    this.authenticationService.registerUser(registerRequest).pipe(take(1)).subscribe({
      next: (response) => {
        let token = response.token;
        sessionStorage.setItem('token', token);
        this.router.navigate(['client']).then();
      },
      error: (err) => {
        this.toastr.error('This email already exists!', 'Error', {
          timeOut: 5000,
          extendedTimeOut: 1000,
          progressBar: true,
          closeButton: true
        });
      }
    });
  }

  isButtonDisabled() {
    let isNameFieldEmpty = this.registerGroup.get('name')!.hasError('required') || this.registerGroup.get('name')!.touched && this.registerGroup.get('name')!.value === '';
    let isEmailFieldEmpty = this.registerGroup.get('email')!.hasError('required') || this.registerGroup.get('email')!.touched && this.registerGroup.get('email')!.value === '';
    let isPasswordFieldEmpty = this.registerGroup.get('password')!.hasError('required') || this.registerGroup.get('password')!.touched && this.registerGroup.get('password')!.value === '';

    return isNameFieldEmpty || isEmailFieldEmpty || isPasswordFieldEmpty;
  }
}
