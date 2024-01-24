package com.localeconnect.app.notification.controller;

import com.localeconnect.app.notification.config.NotificationRabbitConfig;
import com.localeconnect.app.notification.dto.NotificationDTO;
import com.localeconnect.app.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@AllArgsConstructor
//@Slf4j
@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public NotificationDTO greet(NotificationDTO message) throws InterruptedException {
        Thread.sleep(2000);
        return new NotificationDTO(23L, 1L, 2L, LocalDateTime.now(), "test");
    }
}