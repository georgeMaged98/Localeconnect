package com.localeconnect.app.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp-endpoint").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");

    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // Client can connect to this service using this endpoint over websocket connection.
//        registry.addEndpoint("/stomp-endpoint").setAllowedOrigins("*").withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        // This adds a message broker that clients can subscribe to and listen to the messages
//        // which our service(backend) is sending back.
//
//        // This
//        registry.enableSimpleBroker("/topic");
//        // When we register to any of the brokers for any topic, we have to add prefix "/app"
//        registry.setApplicationDestinationPrefixes("/app");
//    }
}
