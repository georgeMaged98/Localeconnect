package com.localeconnect.app.notification.rabbit;

import com.localeconnect.app.notification.config.NotificationRabbitConfig;
import com.localeconnect.app.notification.dto.NotificationDTO;
import com.localeconnect.app.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = NotificationRabbitConfig.QUEUE)
    public void notificationConsumer(NotificationDTO notificationDTO){

        System.out.println("Notification " + notificationDTO);
    }

}
