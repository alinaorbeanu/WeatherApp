import {Component} from '@angular/core';
import {AuthenticationService} from './service/authentication.service';
import {FrameImpl, Stomp} from '@stomp/stompjs';
import {Prediction} from './model/Prediction';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  private stompTrain: any;
  isDisabled: boolean = false;
  predictions: Prediction[] = [];

  constructor(public authenticationService: AuthenticationService, private router: Router) {

    this.stompTrain = Stomp.over(() => new WebSocket('ws://localhost:8083/api/websocket'));
    let that = this;
    this.stompTrain.connect({}, function () {
      that.stompTrain.subscribe('/train-models', function (message: FrameImpl) {
        let webSocketMsg = JSON.parse(message.body);
        that.isDisabled = !that.router.url.includes('details');
        that.predictions = webSocketMsg;
      })
    });
  }

  setPageTitle() {
    if (this.authenticationService.getUserRole() === 'CLIENT') {
      return 'Devices Management';
    }

    let actualPage = sessionStorage.getItem('admin-page')!;

    if (actualPage === null) {
      return '';
    }

    if (actualPage.includes('users')) {
      return 'Users Management';
    } else {
      return 'Devices Management';
    }
  }

  isLogin(isNavBar: boolean) {
    if (this.authenticationService.getToken() === null) {
      return false;
    } else {
      if (isNavBar) {
        return true;
      }

      if (this.authenticationService.getUserRole() === 'CLIENT') {
        this.setPageTitle();
        return true;
      }

      return !(this.authenticationService.getUserRole() === 'ADMIN'
        && sessionStorage.getItem('admin-page') === null);
    }
  }

  isUserAdmin() {
    return this.authenticationService.getUserRole() === 'ADMIN';
  }
}
