package com.localeconnect.app.itinerary.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;
    @NotBlank(message = "Specify the itinerary to be reviewed")
    private Long itineraryId;
    @NotBlank(message = "Specify the reviewer")
    private Long userId;
    @NotBlank(message = "Specify the text of the review")
    @Size(max = 1000, message = "The message is too long, exceeded 1000 characters")
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
}

