import {Injectable} from '@angular/core';
import {LoginRequest} from '../model/LoginRequest';
import {Observable} from 'rxjs';
import {AuthenticationResponse} from '../model/AuthenticationResponse';
import {HttpClient} from '@angular/common/http';
import {RegisterRequest} from '../model/RegisterRequest';
import {JwtService} from './jwt.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private authBaseUrl = 'http://localhost:8080' + '/auth';

  constructor(private httpClient: HttpClient,
              private jwtService: JwtService) {
  }

  loginUser(loginRequest: LoginRequest): Observable<AuthenticationResponse> {
    let loginUrl = this.authBaseUrl + '/login';
    return this.httpClient.post<AuthenticationResponse>(loginUrl, loginRequest, {headers: {'Content-Type': 'application/json'}})
  }

  registerUser(registerRequest: RegisterRequest): Observable<AuthenticationResponse> {
    let registerUrl = this.authBaseUrl + '/register';
    return this.httpClient.post<AuthenticationResponse>(registerUrl, registerRequest, {headers: {'Content-Type': 'application/json'}})
  }

  getUserRole(): string {
    const token = this.getToken();
    if (token.startsWith('ADMIN') || token.startsWith('CLIENT')) {
      return token.split("||")[0];
    }
    if (token) {
      const user = this.jwtService.decodeJwt(token);
      return user.role;
    }
    return '';
  }

  getUsername(): string {
    const token = this.getToken();
    if (token.startsWith('ADMIN') || token.startsWith('CLIENT')) {
      return token.split("||")[1];
    }
    if (token) {
      const user = this.jwtService.decodeJwt(token);
      return user.sub;
    }
    return '';
  }

  getRole() {
    const token = this.getToken();
    const decodedToken = this.jwtService.decodeJwt(token);

    return decodedToken.role;
  }

  getToken(): string {
    return sessionStorage.getItem('token')!;
  }
}
