export class SeenRequest {
  id!: number;
  user!: string;


  constructor(id: number, user: string) {
    this.id = id;
    this.user = user;
  }
}
