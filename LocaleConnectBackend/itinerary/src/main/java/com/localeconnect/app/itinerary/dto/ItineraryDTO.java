package com.localeconnect.app.itinerary.dto;

import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryDTO {

    private String name;
    private String description;
    private int numberOfDays;
    private List<String> tags;
    private List<String> placesToVisit;
    private List<String> dailyActivities;
    private List<String> imageUrls;
    private Long userId;

    public Long getUserId() {
        return userId;
    }
}
