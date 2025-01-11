import {inject, Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from '@angular/router';
import {AuthenticationService} from '../../../service/authentication.service';


@Injectable({
  providedIn: 'root'
})
class PermissionsService {
  constructor(private authService: AuthenticationService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const userRole = this.authService.getUserRole();
    const intendedRoute = this.getIntendedRoute(userRole);

    if (intendedRoute !== state.url) {
      this.router.navigate([intendedRoute]).then(() => {
      });
      return false;
    }
    return true;
  }

  private getIntendedRoute(userRole: string) {
    const page = sessionStorage.getItem('admin-page');
    switch (userRole) {
      case('ADMIN'):
        if (page === 'users') {
          return '/users-management';
        } else {
          return '/device-management';
        }
      case('CLIENT'):
        return '/client';
      default:
        return '/login';
    }
  }
}

export const AuthenticationGuard: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  return inject(PermissionsService).canActivate(next, state);
}
