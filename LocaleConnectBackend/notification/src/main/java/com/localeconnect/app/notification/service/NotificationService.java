package com.localeconnect.app.notification.service;

import com.localeconnect.app.notification.config.NotificationRabbitConfig;
import com.localeconnect.app.notification.dto.NotificationDTO;
import com.localeconnect.app.notification.mapper.NotificationMapper;
import com.localeconnect.app.notification.model.Notification;
import com.localeconnect.app.notification.rabbit.RabbitMQMessageProducer;
import com.localeconnect.app.notification.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationMapper notificationMapper;


    public NotificationDTO handleIncomingNotification(NotificationDTO notificationDTO) {

        Notification notification = notificationMapper.toEntity(notificationDTO);

        // TODO: Check if there exists a websocket connection with notification receiver.
        log.info("TEST: " + notification);
        Notification createdNotification = notificationRepository.save(notification);

        log.info("CREATED NOTIFICATION: " + createdNotification);

        NotificationDTO createdNotificationDTO = notificationMapper.toDomain(createdNotification);

        log.info("CREATED NOTIFICATION DTO 2222: " + createdNotificationDTO);

        // PUSH TO RABBITMQ LISTENER temporarily ->
        // TODO: SHOULD BE REMOVED
//        rabbitMQMessageProducer.publish(createdNotificationDTO, NotificationRabbitConfig.EXCHANGE, NotificationRabbitConfig.ROUTING_KEY);
        simpMessagingTemplate.convertAndSendToUser(notificationDTO.getReceiverID().toString(), "/msg", notificationDTO);
        return createdNotificationDTO;
    }

}
