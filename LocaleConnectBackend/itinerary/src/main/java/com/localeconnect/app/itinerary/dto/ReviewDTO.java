package com.localeconnect.app.itinerary.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;
    private Long itineraryId;
    private Long userId;
    @Size(max = 1000, message = "The message is too long, exceeded 1000 characters")
    private String text;
    private LocalDateTime timestamp;
    @Min(value = 0)
    @Max(value = 5)
    private Double rating;
}

