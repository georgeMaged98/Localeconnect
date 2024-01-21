package com.localeconnect.app.notification.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class NotificationRabbitConfig {

    public static final String QUEUE = "notification.queue";
    public static final String EXCHANGE = "internal.exchange";
    public static final String ROUTING_KEY = "internal.notification.routing-key";
//    @Value("${rabbitmq.exchanges.internal}")
//    private String internalExchange;
//
//    @Value("${rabbitmq.queues.notification}")
//    private String notificationQueue;
//
//    @Value("${rabbitmq.routing-keys.internal-notification}")
//    private String internalNotificationRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Binding internalToNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(internalTopicExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    public String getInternalExchange() {
        return EXCHANGE;
    }

    public String getNotificationQueue() {
        return QUEUE;
    }

    public String getInternalNotificationRoutingKey() {
        return ROUTING_KEY;
    }
}
