package com.localeconnect.app.trip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull(message = "This is a required field")
    private Long sender;

    @NotNull(message = "This is a required field")
    private Long receiver;

    @NotNull(message = "This is a required field")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sentAt;

    @NotBlank(message = "This is a required field")
    private String message;

    private String title;
}

