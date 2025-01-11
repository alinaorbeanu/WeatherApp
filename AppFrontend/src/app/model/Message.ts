export class Message {
  id!: number;

  content!: string;

  sender!: string;

  receiver!: string;

  status!: string;

  localDateTime!: string;

  session!: string;


  constructor(content: string, sender: string, receiver: string, status: string, session: string) {
    this.content = content;
    this.sender = sender;
    this.receiver = receiver;
    this.status = status;
    this.session = session;
  }
}
