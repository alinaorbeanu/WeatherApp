import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/pages/login-component/login.component';
import {RegisterComponent} from './components/pages/register-component/register.component';
import {ClientComponent} from './components/pages/client-component/client.component';
import {AuthenticationGuard} from './components/common/guard/authentication.guard';
import {UsersManagementComponent} from './components/pages/users-management/users-management.component';
import {DeviceManagementComponent} from './components/pages/device-management/device-management.component';
import {DetailsComponent} from './components/pages/details-component/details.component';
import {ChatComponent} from './components/pages/chat-component/chat.component';
import {AddUserComponent} from './components/pages/add-user/add-user.component';
import {UserEditComponent} from './components/pages/user-edit/user-edit.component';
import {AddDeviceComponent} from './components/pages/add-device/add-device.component';
import {DeviceEditComponent} from './components/pages/device-edit/device-edit.component';

const routes: Routes = [
  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'users-management', component: UsersManagementComponent, canActivate: [AuthenticationGuard]},
  {path: 'device-management', component: DeviceManagementComponent, canActivate: [AuthenticationGuard]},
  {path: 'client', component: ClientComponent, canActivate: [AuthenticationGuard]},
  {path: 'details/:id', component: DetailsComponent},
  {path: 'add-user', component: AddUserComponent},
  {path: 'add-device', component: AddDeviceComponent},
  {path: 'edit-user/:id', component: UserEditComponent},
  {path: 'edit-device/:id', component: DeviceEditComponent},
  {path: 'chat', component: ChatComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
