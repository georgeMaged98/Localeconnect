import {Component, OnInit} from '@angular/core';
import {WebSocketService} from "../../service/web-socket.service";
import {Notification} from "../../model/notification";

@Component({
  selector: 'app-detail-header',
  templateUrl: './detail-header.component.html',
  styleUrls: ['./detail-header.component.scss'],
})
export class DetailHeaderComponent implements OnInit {
  notifications: Notification[] = [];
  showNotifications: boolean = false;
  hasNewNotifications: boolean= false;

  constructor(private webSocketService: WebSocketService) {
  }

  ngOnInit() {
    this.webSocketService.getNotifications().subscribe((notifications) => {
      this.notifications = notifications;
      this.hasNewNotifications = notifications.length > 0;

    });
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
    this.hasNewNotifications = false;

    if (!this.showNotifications) {
      this.webSocketService.deleteAllNotifications();
      this.notifications = [];
  }}

}
