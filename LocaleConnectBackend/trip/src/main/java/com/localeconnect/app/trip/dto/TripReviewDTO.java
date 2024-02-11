package com.localeconnect.app.trip.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripReviewDTO {

    private Long id;
    @NotBlank(message = "Specify the trip to be reviewed")
    private Long tripId;
    @NotBlank(message = "Specify the reviewer")
    private Long userId;
    @NotBlank(message = "Specify the text of the review")
    @Size(max = 1000, message = "The message is too long, exceeded 1000 characters")
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
}
