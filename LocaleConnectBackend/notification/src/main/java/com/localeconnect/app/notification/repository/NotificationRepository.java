package com.localeconnect.app.notification.repository;

import com.localeconnect.app.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
