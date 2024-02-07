import { Injectable } from '@angular/core';
// import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  webSocketEndPoint: string = 'http://localhost:8080/ws';
  // topic: string = '/topic/greetings';
  stompClient: any;

  constructor() {
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    console.log('Initialize Websocket Connection!');

    const serverUrl = this.webSocketEndPoint;

    // const ws = new SockJS(serverUrl);
    const ws = new WebSocket('ws://localhost:8080/ws');

    this.stompClient = Stomp.over(ws);
    const that = this;
    const username = Math.random().toString().substring(2, 6);
    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function (frame: Stomp.Frame) {
      that.stompClient.subscribe(
        '/topic/notification',
        (message: Stomp.Message) => {
          console.log(JSON.parse(message.body));
        }
      );
      that.stompClient.subscribe(
        `/user/${username}/msg`,
        (message: Stomp.Message) => {
          console.log(JSON.parse(message.body));
        }
      );
    });
  }

  // sendMessage(message) {
  //   this.stompClient.send('/app/send/message', {}, message);
  // }
}
