import {Component, OnInit} from '@angular/core';
import {Notification} from "../../model/notification";
import {NotificationService} from "../../service/notification.service";
import {AuthService} from "../../service/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-detail-header',
  templateUrl: './detail-header.component.html',
  styleUrls: ['./detail-header.component.scss'],
})
export class DetailHeaderComponent implements OnInit {
  notifications: Notification[] = [];
  showNotifications: boolean = false;
  hasNewNotifications: boolean = false;

  constructor(private notificationService: NotificationService, private authService: AuthService, private router: Router) {
  }

  ngOnInit() {
    this.loadNotifications();
  }

  loadNotifications() {
    this.notificationService.getNotifications().subscribe((notifications) => {
      this.notifications = notifications;
      this.hasNewNotifications = notifications.length > 0;
    });
  }

  toggleNotifications() {
    this.showNotifications = !this.showNotifications;
    this.hasNewNotifications = false;

    if (!this.showNotifications) {
      this.notificationService.deleteAllNotifications();
      this.notifications = [];
    }
  }

  logout() {
    this.authService.logout();
    this.showNotifications = false;
    this.router.navigate(['']).then();
  }

}
