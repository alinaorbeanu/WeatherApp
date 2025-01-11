import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './components/pages/login-component/login.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthenticationService} from './service/authentication.service';
import {RegisterComponent} from './components/pages/register-component/register.component';
import {CommonModule, HashLocationStrategy, LocationStrategy, NgOptimizedImage} from '@angular/common';
import {provideToastr, ToastrModule, ToastrService} from 'ngx-toastr';
import {BrowserAnimationsModule, provideAnimations} from '@angular/platform-browser/animations';
import {DeviceManagementComponent} from './components/pages/device-management/device-management.component';
import {ClientComponent} from './components/pages/client-component/client.component';
import {ConfirmationDialogComponent} from './components/common/confirmation-dialog/confirmation-dialog.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {UsersManagementComponent} from './components/pages/users-management/users-management.component';
import {DetailsComponent} from './components/pages/details-component/details.component';
import {CalendarModule} from 'primeng/calendar';
import {ChatComponent} from './components/pages/chat-component/chat.component';
import {NavbarComponent} from './components/common/navbar/navbar.component';
import {SideNavbarComponent} from './components/common/side-navbar/side-navbar.component';
import {AddUserComponent} from './components/pages/add-user/add-user.component';
import {UserCardComponent} from './components/common/user-card/user-card.component';
import {UserEditComponent} from './components/pages/user-edit/user-edit.component';
import {DeviceCardComponent} from './components/common/device-card/device-card.component';
import {AddDeviceComponent} from './components/pages/add-device/add-device.component';
import {DeviceEditComponent} from './components/pages/device-edit/device-edit.component';
import {ChatCardComponent} from './components/common/chat-card/chat-card.component';
import {ShowMessagePipe} from './components/common/pipes/show-message.pipe';
import {ChatGridComponent} from './components/common/chat-grid/chat-grid.component';
import {CalendarCardComponent} from './components/common/calendar-card/calendar-card.component';
import {InformationCardComponent} from './components/common/information-card/information-card.component';
import {SpinnerComponent} from './components/common/spinner/spinner.component';
import {NotificationTrainComponent} from './components/common/notification-train/notification-train.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    DeviceManagementComponent,
    ClientComponent,
    ConfirmationDialogComponent,
    UsersManagementComponent,
    DetailsComponent,
    ChatComponent,
    NavbarComponent,
    SideNavbarComponent,
    AddUserComponent,
    UserCardComponent,
    UserEditComponent,
    DeviceCardComponent,
    AddDeviceComponent,
    DeviceEditComponent,
    ChatCardComponent,
    ShowMessagePipe,
    ChatGridComponent,
    CalendarCardComponent,
    InformationCardComponent,
    SpinnerComponent,
    NotificationTrainComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CommonModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule,
    NgbModule,
    CalendarModule,
    NgOptimizedImage
  ],
  providers: [
    AuthenticationService,
    ToastrService,
    provideAnimations(),
    provideToastr(),
    {provide: LocationStrategy, useClass: HashLocationStrategy}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
