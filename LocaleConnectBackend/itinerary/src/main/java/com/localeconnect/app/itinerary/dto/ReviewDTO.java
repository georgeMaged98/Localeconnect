package com.localeconnect.app.itinerary.dto;
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
    private String text;
    private LocalDateTime timestamp;
    private Double rating;
}

