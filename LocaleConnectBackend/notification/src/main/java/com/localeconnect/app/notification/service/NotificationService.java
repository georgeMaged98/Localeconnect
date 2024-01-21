package com.localeconnect.app.notification.service;

import com.localeconnect.app.notification.mapper.NotificationMapper;
import com.localeconnect.app.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;



}
