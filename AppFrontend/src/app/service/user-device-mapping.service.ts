import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {AuthenticationService} from "./authentication.service";
import {Device} from "../model/Device";

@Injectable({
  providedIn: 'root'
})
export class UserDeviceMappingService {
  private authBaseUrl = 'http://localhost:8080' + '/user/device';

  constructor(private httpClient: HttpClient,
              private authService: AuthenticationService) {
  }

  findAllUserDevices(): Observable<Device[]> {
    let username = this.authService.getUsername();
    let uerDeviceMappingUrl = this.authBaseUrl + '/' + username;
    return this.httpClient.get<Device[]>(uerDeviceMappingUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    })
  }
}
