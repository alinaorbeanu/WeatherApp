import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {User} from '../model/User';
import {AuthenticationService} from "./authentication.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/user';

  constructor(private http: HttpClient,
              private authService: AuthenticationService) {
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findUserById(userId: number): Observable<User> {
    return this.http.get<User>(this.apiUrl + '/' + userId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findUserByEmail(userEmail: string): Observable<User> {
    return this.http.get<User>(this.apiUrl + '/email/' + userEmail, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + '/' + userId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(this.apiUrl, user, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }
}
