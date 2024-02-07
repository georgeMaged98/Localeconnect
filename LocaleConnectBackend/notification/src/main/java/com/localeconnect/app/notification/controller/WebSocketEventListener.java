package com.localeconnect.app.notification.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@AllArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
//        String username = headers.getUser().getName();
        log.info("USER CONNECTED!! ");
//        System.out.println(username);
    }

    @EventListener
    public void handleSessionConnected2(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
//        String username = headers.getUser().getName();
        System.out.println("USER CONNECTED2222!! ");
        log.info(headers.toString());
//        System.out.println(username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("USER DISCONNECTED!!!");
        System.out.println(event);
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//        if(username != null) {
////            logger.info("User Disconnected : " + username);
//
//            //remove user from latest Location Feed
////            CommunicationController.latestLocationFeed.remove(username);
//
//            //transmitting current user's latest location feed
//            messagingTemplate.convertAndSend("/app/getData", new LocationBean());
//        }
    }
}
