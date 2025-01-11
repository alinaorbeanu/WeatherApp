import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtService {

  decodeJwt(token: string) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64));
    return JSON.parse(jsonPayload);
  }
}
