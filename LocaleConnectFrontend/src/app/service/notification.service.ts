import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Notification} from "../model/notification";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  public stompClient: any;
  private baseUrl = 'http://localhost:8080/ws'; // TODO: update with current url
  public notifications: BehaviorSubject<Notification[]> | undefined;
  private notificationsMock = new BehaviorSubject<Notification[]>([
    {
      id: 1,
      senderID: 100,
      receiverID: 200,
      sentAt: new Date('10-10-2023 14:00:00'),
      message: 'Welcome to LocaleConnect!',
      title: 'Welcome',
    },
    {
      id: 2,
      senderID: 101,
      receiverID: 200,
      sentAt: new Date('11-10-2023 09:30:00'),
      message: 'Your profile has been updated successfully.',
      title: 'Profile Update',
    },

  ]);

  constructor(private snackBar: MatSnackBar) {
    //TODO: Add websocket logic
    // this.initializeWebSocketConnection();
  }

  /*  initializeWebSocketConnection() {
  const serverUrl = this.baseUrl;
    const ws = new SockJS(serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    this.stompClient.connect({}, function(frame: any) {
      that.stompClient.subscribe('/topic/notification', (message: any) => {
        if (message.body) {
          let notification = JSON.parse(message.body);
          let currentNotifications = that.notifications.value;
          currentNotifications.push(notification);
          that.notifications.next(currentNotifications);
        }
      });
    });
  }

  disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
  }

  sendMessage(notification: Notification) {
    this.stompClient.send('/app/notify', {}, JSON.stringify(notification));
  }

   */
  getNotifications(): Observable<Notification[]> {
    return this.notificationsMock.asObservable();
  }

//TODO: replace with api call
  addNotification(notification: Notification) {
    const currentNotifications = this.notificationsMock.value;
    this.notificationsMock.next([...currentNotifications, notification]);
  }
//TODO: replace with api call
  deleteAllNotifications() {
    this.notificationsMock.next([]);
  }

  showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['success-snackbar']
    });
  }

  showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar'],

    });
  }
}
