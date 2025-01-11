import {Role} from './Role';

export  class RegisterRequest {
  name!: string;
  email!: string;
  password!: string;
  roleDTO!: Role;
}
