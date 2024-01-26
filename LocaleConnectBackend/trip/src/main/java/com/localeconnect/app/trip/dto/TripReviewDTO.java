package com.localeconnect.app.trip.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripReviewDTO {
    private Long tripReviewId;
    private Long tripId;
    private Long userId;
    private String text;
    private LocalDateTime timestamp;
    private Double rating;

}
