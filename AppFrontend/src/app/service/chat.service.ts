import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {SessionChat} from '../model/SessionChat';
import {Message} from '../model/Message';
import {SeenRequest} from '../model/SeenRequest';
import {AuthenticationService} from './authentication.service';
import {UserChat} from '../model/UserChat';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private sessionBaseUrl = 'http://localhost:8086/session/chat';
  private messageBaseUrl = 'http://localhost:8086/message';

  constructor(private httpClient: HttpClient,
              private authService: AuthenticationService) {
  }

  findAllByReceiverAndSenderAndStatus(receiver: string, sender: string): Observable<SessionChat> {
    return this.httpClient.get<SessionChat>(this.sessionBaseUrl + '/' + receiver + '/' + sender, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findAllByClientOrAdminAndStatus(username: string): Observable<UserChat[]> {
    return this.httpClient.get<UserChat[]>(this.sessionBaseUrl + '/username/' + username, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findAllOpenedChatsByUsernameAndStatus(username: string): Observable<number[]> {
    return this.httpClient.get<number[]>(this.sessionBaseUrl + '/open/' + username, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  findAllUnreadMessagesByChatsAndUsername(chats: number[], username: string): Observable<number[]> {
    return this.httpClient.get<number[]>(this.messageBaseUrl + '/unread/' + chats + '/' + username, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  closeSessionStatus(id: number): Observable<any> {
    return this.httpClient.put<any>(this.sessionBaseUrl + '/close', id, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  sendMessage(message: Message): Observable<any> {
    return this.httpClient.post(this.messageBaseUrl, message, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  setOnSeenMessage(seenRequest: SeenRequest): Observable<any> {
    return this.httpClient.put(this.messageBaseUrl, seenRequest, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  createNewSession(sessionChat: SessionChat): Observable<any> {
    return this.httpClient.post(this.sessionBaseUrl, sessionChat, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }

  isTypingNotification(message: Message): Observable<any> {
    return this.httpClient.post(this.messageBaseUrl + '/isTyping', message, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + this.authService.getToken()
      }
    });
  }
}
