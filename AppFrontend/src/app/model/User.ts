import {Role} from './Role';

export class User {
  id!: number;
  name!: string;
  email!: string;
  password!: string;
  roleDTO!: Role;
  role!: string;
}
