package com.localeconnect.app.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "sender_id")
//    private Long senderID;

    @Column(name = "receiver_id")
    private Long receiverID;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    private String title;
    private String message;

//    private String metadata;

    private boolean isRead;
}
