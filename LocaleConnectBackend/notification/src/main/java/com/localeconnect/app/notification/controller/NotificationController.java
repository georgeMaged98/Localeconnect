package com.localeconnect.app.notification.controller;

import com.localeconnect.app.notification.config.NotificationRabbitConfig;
import com.localeconnect.app.notification.dto.NotificationDTO;
import com.localeconnect.app.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/")
    public void sendNotification(@RequestBody NotificationDTO notificationDTO) {
//        log.info("New notification... {}", notificationRequest);
        System.out.println("GGGGGGGGG");
        rabbitTemplate.convertAndSend(NotificationRabbitConfig.EXCHANGE, NotificationRabbitConfig.ROUTING_KEY, notificationDTO);
    }
}