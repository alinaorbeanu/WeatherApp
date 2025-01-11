import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Prediction} from '../model/Prediction';
import {AuthenticationService} from './authentication.service';
import {PredictionValue} from '../model/PredictionValue';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {

  private apiUrl = 'http://localhost:8083/weather/';

  constructor(private http: HttpClient,
              private authService: AuthenticationService) {
  }

  trainModels(): Observable<Prediction[]> {
    return this.http.get<Prediction[]>(this.apiUrl + 'train', {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  predictOnPeriod(start: string, end: string, deviceNumber: number): Observable<PredictionValue[]> {
    return this.http.get<PredictionValue[]>(this.apiUrl + 'predict/start/' + start + '/end/' + end + '/deviceNumber/' + deviceNumber, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }
}
