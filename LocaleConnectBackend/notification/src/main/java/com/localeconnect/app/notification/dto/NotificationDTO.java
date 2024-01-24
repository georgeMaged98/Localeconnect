package com.localeconnect.app.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationDTO {

    private Long id;

    @NotNull(message = "This is a required field")
    private Long senderID;

    @NotNull(message = "This is a required field")
    private Long receiverID;

    @NotNull(message = "This is a required field")
    private LocalDateTime sentAt;

    @NotBlank(message = "This is a required field")
    private String message;
}
