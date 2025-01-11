import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Device} from '../model/Device';
import {AuthenticationService} from "./authentication.service";

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  private deviceBaseUrl = 'http://localhost:8080/device';

  constructor(private http: HttpClient,
              private authService: AuthenticationService) {
  }

  addDevice(device: Device): Observable<Device> {
    return this.http.post<Device>(this.deviceBaseUrl, device, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findDeviceById(deviceId: number): Observable<Device> {
    return this.http.get<Device>(this.deviceBaseUrl + '/' + deviceId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findAllDevices(): Observable<Device[]> {
    return this.http.get<Device[]>(this.deviceBaseUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  deleteDevice(deviceId: number): Observable<void> {
    return this.http.delete<void>(this.deviceBaseUrl + '/' + deviceId, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  updateDevice(device: Device): Observable<Device> {
    return this.http.put<Device>(this.deviceBaseUrl, device, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }
}
