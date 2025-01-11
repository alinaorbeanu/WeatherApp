export class WebSocketMsg {

  message!: string;
  userName!: string;

  sender!: string;


  constructor(message: string, userName: string) {
    this.message = message;
    this.userName = userName;
  }

}
