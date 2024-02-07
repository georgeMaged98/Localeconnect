package com.localeconnect.app.notification.controller;

import com.localeconnect.app.notification.config.NotificationRabbitConfig;
import com.localeconnect.app.notification.dto.NotificationDTO;
import com.localeconnect.app.notification.rabbit.RabbitMQMessageProducer;
import com.localeconnect.app.notification.response_handler.ResponseHandler;
import com.localeconnect.app.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private SimpMessagingTemplate messagingTemplate;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @GetMapping("/notify")
    public ResponseEntity<Object> getNotification() throws InterruptedException {
        NotificationDTO notificationDTO = new NotificationDTO(23L, 1L, 2L, LocalDateTime.now(), "test", "TITLE");
        messagingTemplate.convertAndSend("/topic/notification", notificationDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, notificationDTO, null);
    }

    @PostMapping("/notify")
    public ResponseEntity<Object> sendNotification(@RequestBody @Valid NotificationDTO incomingNotificationDTO) {
//        NotificationDTO notificationDTO = new NotificationDTO(23L, 1L, 2L, LocalDateTime.now(), "test");
//        messagingTemplate.convertAndSend("/topic/notification", notificationDTO);

//        rabbitMQMessageProducer.publish(incomingNotificationDTO, NotificationRabbitConfig.EXCHANGE, NotificationRabbitConfig.ROUTING_KEY);

        NotificationDTO newNotificationDTO = notificationService.handleIncomingNotification(incomingNotificationDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, newNotificationDTO, null);
    }
}