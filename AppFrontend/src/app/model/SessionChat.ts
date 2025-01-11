import {Message} from "./Message";

export class SessionChat {
  id!: number;

  status!: string;

  client!: string;

  admin!: string;

  messages!: Message[];


  constructor(client: string) {
    this.client = client;
  }
}
